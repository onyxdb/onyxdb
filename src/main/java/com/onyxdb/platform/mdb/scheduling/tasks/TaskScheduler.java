package com.onyxdb.platform.mdb.scheduling.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.mdb.scheduling.operations.OperationRepository;
import com.onyxdb.platform.mdb.scheduling.operations.models.Operation;
import com.onyxdb.platform.mdb.scheduling.operations.models.OperationStatus;
import com.onyxdb.platform.mdb.scheduling.tasks.models.ProducedTask;
import com.onyxdb.platform.mdb.scheduling.tasks.models.SchedulingResult;
import com.onyxdb.platform.mdb.scheduling.tasks.models.Task;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskFilter;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskStatus;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskType;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskWithBlockers;
import com.onyxdb.platform.mdb.scheduling.tasks.producers.TaskProducerProvider;
import com.onyxdb.platform.mdb.utils.ObjectMapperUtils;
import com.onyxdb.platform.mdb.utils.TimeUtils;

@Service("OnyxdbTaskScheduler")
@RequiredArgsConstructor
public class TaskScheduler {
    private static final int DEFAULT_TASK_MAX_ATTEMPTS = 5;
    private static final int DEFAULT_TASK_POST_DELAY_SECONDS = 5;

    private static final Set<TaskType> LONG_TASK_TYPES = Set.of(
            TaskType.MONGO_APPLY_PSMDB,
            TaskType.MONGO_APPLY_ONYXDB_AGENT
    );

    private final OperationRepository operationRepository;
    private final TaskRepository taskRepository;
    private final TransactionTemplate transactionTemplate;
    private final ObjectMapper objectMapper;
    private final TaskProducerProvider taskProducerProvider;

    public List<Task> getTasksToConsume(int limit, LocalDateTime scheduledAt) {
        return taskRepository.getTasksToConsume(limit, scheduledAt);
    }

    public void markTaskAsStarted(Task task) {
        var updatedTask = Task.builder()
                .copy(task)
                .withStatus(TaskStatus.IN_PROGRESS)
                .withStartedAt(TimeUtils.now())
                .build();
        taskRepository.updateTask(updatedTask);
    }

    public void markTaskAsFinished(Task task) {
        var updatedTask = Task.builder()
                .copy(task)
                .withStatus(TaskStatus.SUCCESS)
                .withFinishedAt(TimeUtils.now())
                .build();
        taskRepository.updateTask(updatedTask);
    }

    public void rescheduleTask(Task task) {
        int updatedAttemptsLeft = task.attemptsLeft() - 1;
        if (updatedAttemptsLeft <= 0) {
            var updatedTask = Task.builder()
                    .copy(task)
                    .withStatus(TaskStatus.ERROR)
                    .withAttemptsLeft(0)
                    .build();

            transactionTemplate.executeWithoutResult(status -> {
                taskRepository.updateTask(updatedTask);
                operationRepository.updateStatus(task.operationId(), OperationStatus.ERROR);
            });
            return;
        }

        var updatedRequestedTask = Task.builder()
                .copy(task)
                .withStatus(TaskStatus.SCHEDULED)
                .withAttemptsLeft(updatedAttemptsLeft)
                .withScheduledAt(TimeUtils.now().plus(Duration.ofSeconds(DEFAULT_TASK_POST_DELAY_SECONDS)))
                .build();

        List<TaskWithBlockers> scheduledTasksWithBlockers = taskRepository.listTasksWithBlockers(
                TaskFilter.builder()
                        .withOperationIds(List.of(task.operationId()))
                        .build()
        );
        List<TaskWithBlockers> sortedTasksWithBlockers = TaskUtils.sortTasksWithBlockers(scheduledTasksWithBlockers);
        List<Task> updatedTasks = new ArrayList<>(sortedTasksWithBlockers.stream().map(TaskWithBlockers::task).toList());
        System.err.println("found tasks: " + updatedTasks);


        int indexOfRequestedTask = 0;
        for (int i = 0; i < updatedTasks.size(); i++) {
            Task currentTask = updatedTasks.get(i);
            @Nullable
            Task previousTask = i > 0 ? updatedTasks.get(i - 1) : null;

            boolean isRequestedTask = currentTask.id().equals(task.id());
            if (isRequestedTask) {
                indexOfRequestedTask = i;
                System.err.println("Setting updatedRequestedTask: " + updatedRequestedTask);
                updatedTasks.set(i, updatedRequestedTask);
                continue;
            }

            if (previousTask != null) {
                SchedulingResult schedulingResult = scheduleTaskByPrevious(
                        previousTask.type(),
                        previousTask.scheduledAt()
                );

                var updatedTask = Task.builder()
                        .copy(currentTask)
                        .withScheduledAt(schedulingResult.scheduledAt())
                        .build();

                System.err.printf("i={%d}, updatedTask={%s}%n", i, updatedTask);

                updatedTasks.set(i, updatedTask);
            } else {
                Task updatedTask = Task.builder()
                        .copy(currentTask)
                        .withScheduledAt(TimeUtils.now().plus(Duration.ofSeconds(DEFAULT_TASK_POST_DELAY_SECONDS)))
                        .build();

                System.err.printf("i={%d}, updatedTask={%s}%n", i, updatedTask);

                updatedTasks.set(i, updatedTask);
            }
        }

        taskRepository.updateTasks(updatedTasks.subList(indexOfRequestedTask, updatedTasks.size()));
    }

    public void registerOperation(Operation operation) {
        List<ProducedTask> producedTasks = taskProducerProvider.getTaskProducerOrThrow(operation.type())
                .produceTasks(operation, operation.payload());

        List<TaskWithBlockers> tasksWithBlockers = convertProducedTasksToTasksWithBlockers(producedTasks);
        transactionTemplate.executeWithoutResult(status -> {
            List<Task> tasks = tasksWithBlockers.stream().map(TaskWithBlockers::task).toList();
            taskRepository.createBulk(tasks);
            taskRepository.createBlockerTasksBulk(tasksWithBlockers);
            operationRepository.updateStatus(operation.id(), OperationStatus.IN_PROGRESS);
        });
    }

    private List<TaskWithBlockers> convertProducedTasksToTasksWithBlockers(List<ProducedTask> producedTasks) {
        List<TaskWithBlockers> tasksWithBlockers = new ArrayList<>(producedTasks.size());

        for (int i = 0; i < producedTasks.size(); i++) {
            ProducedTask producedTask = producedTasks.get(i);

            LocalDateTime scheduledAt = TimeUtils.now();
            int attempts = DEFAULT_TASK_MAX_ATTEMPTS;
            if (i > 0) {
                TaskWithBlockers previousTask = tasksWithBlockers.get(i - 1);
                SchedulingResult schedulingResult = scheduleTaskByPrevious(
                        previousTask.task().type(),
                        previousTask.task().scheduledAt()
                );

                scheduledAt = schedulingResult.scheduledAt();
                attempts = schedulingResult.attempts();
            }

            tasksWithBlockers.add(new TaskWithBlockers(
                    Task.create(
                            producedTask.id(),
                            producedTask.type(),
                            TaskStatus.SCHEDULED,
                            producedTask.operationId(),
                            scheduledAt,
                            attempts,
                            ObjectMapperUtils.convertToString(objectMapper, producedTask.payload())
                    ),
                    producedTask.blockerIds()
            ));
        }

        return tasksWithBlockers;
    }

    private static SchedulingResult scheduleTaskByPrevious(
            TaskType previousTaskType,
            LocalDateTime previousScheduledAt
    ) {
        int attempts = DEFAULT_TASK_MAX_ATTEMPTS;
        int postDelaySeconds = DEFAULT_TASK_POST_DELAY_SECONDS;

        if (previousTaskType != null) {
            if (LONG_TASK_TYPES.contains(previousTaskType)) {
                attempts = 10;
                postDelaySeconds = 30;
                // 10 * 30 = 300s
            }
        }

        return new SchedulingResult(
                previousScheduledAt.plus(Duration.ofSeconds(postDelaySeconds)),
                attempts
        );
    }
}
