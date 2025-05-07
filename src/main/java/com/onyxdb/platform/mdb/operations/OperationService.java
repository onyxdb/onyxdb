package com.onyxdb.platform.mdb.operations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.mdb.exceptions.OperationNotFoundException;
import com.onyxdb.platform.mdb.operations.mappers.TaskMapper;
import com.onyxdb.platform.mdb.operations.models.Operation;
import com.onyxdb.platform.mdb.operations.models.OperationStatus;
import com.onyxdb.platform.mdb.operations.models.ProducedTask;
import com.onyxdb.platform.mdb.operations.models.Task;
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

    private static final Set<TaskType> DEPLOY_TASK_TYPES = Set.of(
            TaskType.MONGO_APPLY_PSMDB,
            TaskType.MONGO_APPLY_ONYXDB_AGENT
    );

    private static final Set<TaskType> CHECK_RESULT_TASK_TYPES = Set.of(
            TaskType.MONGO_CHECK_PSMDB_READINESS,
            TaskType.MONGO_APPLY_ONYXDB_AGENT
    );

    private final OperationRepository operationRepository;
    private final TaskRepository taskRepository;
    private final TransactionTemplate transactionTemplate;
    private final TaskProducerProvider taskProducerProvider;
    private final TaskMapper taskMapper;

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
        taskRepository.updateTask(updatedTask);
    }

    public void registerOperation(Operation operation) {
        List<ProducedTask> producedTasks = taskProducerProvider.getTaskProducerOrThrow(operation.type())
                .produceTasks(operation, operation.payload());

        List<Task> tasks = convertProducedTasksToTasks(producedTasks);
        transactionTemplate.executeWithoutResult(status -> {
            taskRepository.createTasks(tasks);
            operationRepository.updateStatus(operation.id(), OperationStatus.IN_PROGRESS);
        });
    }

    private List<Task> convertProducedTasksToTasks(List<ProducedTask> producedTasks) {
        List<Task> tasks = new ArrayList<>(producedTasks.size());

        for (ProducedTask producedTask : producedTasks) {
            int attempts = DEFAULT_TASK_MAX_ATTEMPTS;
            int postDelaySeconds = DEFAULT_TASK_POST_DELAY_SECONDS;
            int retryDelaySeconds = DEFAULT_TASK_RETRY_DELAY_SECONDS;

            if (CHECK_RESULT_TASK_TYPES.contains(producedTask.type())) {
                attempts = 60;
                retryDelaySeconds = 5;
            }

            Task task = taskMapper.producedTaskToTask(
                    producedTask,
                    attempts,
                    postDelaySeconds,
                    retryDelaySeconds
            );
            tasks.add(task);
        }

        return tasks;
    }

    public List<Operation> listOperations(UUID clusterId) {
        return operationRepository.listOperations(clusterId, null);
    }

    public Optional<Operation> getOperationForUpdateO(UUID operationId) {
        return operationRepository.getForUpdateO(operationId);
    }

    public Operation getOperationForUpdate(UUID operationId) {
        return getOperationForUpdateO(operationId).orElseThrow(() -> new OperationNotFoundException(operationId));
    }

    public void restartOperation(UUID operationId) {
//        transactionTemplate.executeWithoutResult(status -> {
//            Operation operation = getOperationForUpdate(operationId);
//
//            if (!operation.isRestartAllowed()) {
//                throw new OperationRestartNotAllowedException(operationId);
//            }
//
//            var failedTasksFilter = TaskFilter.builder()
//                    .withOperationIds(List.of(operationId))
//                    .withStatuses(List.of(TaskStatus.ERROR))
//                    .build();
//
//            List<Task> failedTasks = taskRepository.listTasks(failedTasksFilter);
//            List<Task> scheduledTasks = failedTasks.stream().map(failedTask -> Task.builder()
//                    .copy(failedTask)
//                    .withStatus(TaskStatus.SCHEDULED)
//                    //TODO get needed attemptsLeft from single place for generating and restarting
//                    .withAttemptsLeft(5)
//                    .build()
//            ).toList();
//
//            taskRepository.updateTasks(scheduledTasks);
//            operationRepository.updateStatus(operationId, OperationStatus.IN_PROGRESS);
//        });
    }
}
