package com.onyxdb.platform.mdb.operations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.mdb.clusters.ClusterRepository;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.clusters.models.ClusterStatus;
import com.onyxdb.platform.mdb.exceptions.OperationNotFoundException;
import com.onyxdb.platform.mdb.exceptions.OperationRestartNotAllowedException;
import com.onyxdb.platform.mdb.operations.mappers.TaskMapper;
import com.onyxdb.platform.mdb.operations.models.Operation;
import com.onyxdb.platform.mdb.operations.models.OperationStatus;
import com.onyxdb.platform.mdb.operations.models.ProducedTask;
import com.onyxdb.platform.mdb.operations.models.SchedulingResult;
import com.onyxdb.platform.mdb.operations.models.Task;
import com.onyxdb.platform.mdb.operations.models.TaskFilter;
import com.onyxdb.platform.mdb.operations.models.TaskStatus;
import com.onyxdb.platform.mdb.operations.models.TaskType;
import com.onyxdb.platform.mdb.operations.producers.TaskProducerProvider;
import com.onyxdb.platform.mdb.operations.repositories.OperationRepository;
import com.onyxdb.platform.mdb.operations.repositories.TaskRepository;
import com.onyxdb.platform.mdb.utils.TimeUtils;

@Service
@RequiredArgsConstructor
public class OperationService {
    private static final int DEFAULT_TASK_MAX_ATTEMPTS = 5;
    private static final int DEFAULT_TASK_POST_DELAY_SECONDS = 5;
    private static final int DEFAULT_TASK_RETRY_DELAY_SECONDS = 5;

    private static final Set<TaskType> CHECK_RESULT_TASK_TYPES = Set.of(
            TaskType.MONGO_CHECK_PSMDB_READINESS,
            TaskType.MONGO_APPLY_ONYXDB_AGENT,
            TaskType.MONGO_CHECK_ONYXDB_AGENT_IS_DELETED,
            TaskType.MONGO_CHECK_PSMDB_IS_DELETED,
            TaskType.MONGO_CHECK_BACKUP_IS_READY,
            TaskType.MONGO_CHECK_BACKUP_IS_DELETED
    );

    private final OperationRepository operationRepository;
    private final TaskRepository taskRepository;
    private final TransactionTemplate transactionTemplate;
    private final TaskProducerProvider taskProducerProvider;
    private final TaskMapper taskMapper;
    private final ClusterRepository clusterRepository;

    public List<Task> getTasksToConsume(int limit) {
        return taskRepository.getTasksToConsume(limit);
    }

    public Task markTaskAsStarted(Task task) {
        LocalDateTime now = TimeUtils.now();
        var updatedTask = Task.builder()
                .copy(task)
                .status(TaskStatus.IN_PROGRESS)
                .updatedAt(now)
                .startedAt(now)
                .build();
        taskRepository.updateTask(updatedTask);

        return updatedTask;
    }

    public void markTaskAsFinished(Task task) {
        LocalDateTime now = TimeUtils.now();
        var updatedTask = Task.builder()
                .copy(task)
                .status(TaskStatus.SUCCESS)
                .updatedAt(now)
                .finishedAt(now)
                .build();
        taskRepository.updateTask(updatedTask);
    }

    public void rescheduleTask(Task task) {
        Operation operation = operationRepository.getOperation(task.operationId());

        int updatedAttempts = Math.max(task.attemptsLeft() - 1, 0);
        TaskStatus updatedStatus = updatedAttempts > 0 ? TaskStatus.RESCHEDULED : TaskStatus.ERROR;
        LocalDateTime now = TimeUtils.now();

        var updatedTask = Task.builder()
                .copy(task)
                .updatedAt(now)
                .attemptsLeft(updatedAttempts)
                .status(updatedStatus)
                .finishedAt(now)
                .build();
        if (updatedStatus.equalsStringEnum(TaskStatus.ERROR)) {
            transactionTemplate.executeWithoutResult(status -> {
                if (operation.clusterId() != null) {
                    Cluster cluster = clusterRepository.getClusterOrThrow(operation.clusterId());
                    Cluster updatedCluster = Cluster.builder()
                            .copy(cluster)
                            .status(ClusterStatus.ERROR)
                            .build();
                    clusterRepository.updateCluster(updatedCluster);
                }
                taskRepository.updateTask(updatedTask);
                operationRepository.updateStatus(updatedTask.operationId(), OperationStatus.ERROR);
            });
        } else {
            taskRepository.updateTask(updatedTask);
        }
    }

    public void registerOperation(Operation operation) {
        List<ProducedTask> producedTasks = taskProducerProvider.getTaskProducerOrThrow(operation.type())
                .produceTasks(operation, operation.payload());

        List<Task> tasks = scheduleProducedTasks(producedTasks);
        transactionTemplate.executeWithoutResult(status -> {
            taskRepository.createTasks(tasks);
            operationRepository.updateStatus(operation.id(), OperationStatus.IN_PROGRESS);
        });
    }

    public List<Operation> listOperations(@Nullable UUID clusterId) {
        return operationRepository.listOperations(clusterId, null);
    }

    public Optional<Operation> getOperationForUpdateO(UUID operationId) {
        return operationRepository.getForUpdateO(operationId);
    }

    public Operation getOperationForUpdate(UUID operationId) {
        return getOperationForUpdateO(operationId).orElseThrow(() -> new OperationNotFoundException(operationId));
    }

    public void restartOperation(UUID operationId) {
        transactionTemplate.executeWithoutResult(status -> {
            Operation operation = getOperationForUpdate(operationId);

            if (!operation.isRestartAllowed()) {
                throw new OperationRestartNotAllowedException(operationId);
            }

            var failedTasksFilter = TaskFilter.builder()
                    .withOperationIds(List.of(operationId))
                    .withStatuses(List.of(TaskStatus.ERROR))
                    .build();
            List<Task> failedTasks = taskRepository.listTasks(failedTasksFilter);
            List<Task> rescheduleTasks = rescheduleFailedTasks(failedTasks);

            taskRepository.updateTasks(rescheduleTasks);
            operationRepository.updateStatus(operationId, OperationStatus.IN_PROGRESS);
        });
    }

    private List<Task> rescheduleFailedTasks(List<Task> failedTasks) {
        List<Task> rescheduledTasks = new ArrayList<>(failedTasks.size());
        for (Task task : failedTasks) {
            SchedulingResult schedulingResult = scheduleTask(task.type());

            Task rescheduledTask = Task.builder()
                    .copy(task)
                    .status(TaskStatus.RESCHEDULED)
                    .attemptsLeft(schedulingResult.attempts())
                    .postDelaySeconds(schedulingResult.postDelaySeconds())
                    .retryDelaySeconds(schedulingResult.retryDelaySeconds())
                    .build();

            rescheduledTasks.add(rescheduledTask);
        }

        return rescheduledTasks;
    }

    private List<Task> scheduleProducedTasks(List<ProducedTask> producedTasks) {
        List<Task> tasks = new ArrayList<>(producedTasks.size());

        for (ProducedTask producedTask : producedTasks) {
            SchedulingResult schedulingResult = scheduleTask(producedTask.type());
            Task task = taskMapper.producedTaskToTask(
                    producedTask,
                    schedulingResult.attempts(),
                    schedulingResult.postDelaySeconds(),
                    schedulingResult.retryDelaySeconds()
            );
            tasks.add(task);
        }

        return tasks;
    }

    private SchedulingResult scheduleTask(TaskType taskType) {
        int attempts = DEFAULT_TASK_MAX_ATTEMPTS;
        int postDelaySeconds = DEFAULT_TASK_POST_DELAY_SECONDS;
        int retryDelaySeconds = DEFAULT_TASK_RETRY_DELAY_SECONDS;

        if (CHECK_RESULT_TASK_TYPES.contains(taskType)) {
            attempts = 60;
            retryDelaySeconds = 5;
        }

        return new SchedulingResult(
                attempts,
                postDelaySeconds,
                retryDelaySeconds
        );
    }
}
