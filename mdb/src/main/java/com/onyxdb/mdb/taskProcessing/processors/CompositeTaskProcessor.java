package com.onyxdb.mdb.taskProcessing.processors;

import java.time.Duration;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.onyxdb.mdb.core.clusters.ClusterService;
import com.onyxdb.mdb.exceptions.ClusterTaskHasNoAttemptsException;
import com.onyxdb.mdb.exceptions.InternalServerErrorException;
import com.onyxdb.mdb.services.BaseClusterService;
import com.onyxdb.mdb.taskProcessing.models.OperationStatus;
import com.onyxdb.mdb.taskProcessing.models.Task;
import com.onyxdb.mdb.taskProcessing.models.TaskProcessingResult;
import com.onyxdb.mdb.taskProcessing.models.TaskStatus;
import com.onyxdb.mdb.taskProcessing.models.TaskType;

/**
 * @author foxleren
 */
public class CompositeTaskProcessor {
    private static final Logger logger = LoggerFactory.getLogger(CompositeTaskProcessor.class);

    private static final Duration DEFAULT_SCHEDULED_AT_DELAY = Duration.ofSeconds(10);

    private final Map<TaskType, TaskProcessor<?>> taskTypeToTaskProcessors;
    private final BaseClusterService clusterServiceOld;
    private final ClusterService clusterServiceNew;

    public CompositeTaskProcessor(
            Map<TaskType, TaskProcessor<?>> taskTypeToTaskProcessors,
            BaseClusterService clusterServiceOld,
            ClusterService clusterServiceNew
    ) {
        this.taskTypeToTaskProcessors = taskTypeToTaskProcessors;
        this.clusterServiceOld = clusterServiceOld;
        this.clusterServiceNew = clusterServiceNew;
    }

    public void processTask(Task task) {
        try {
            if (!taskTypeToTaskProcessors.containsKey(task.type())) {
                throw new InternalServerErrorException(String.format(
                        "Can't find processor for task type %s", task.type()
                ));
            }

            TaskProcessor<?> taskProcessor = taskTypeToTaskProcessors.get(task.type());

            if (task.isFirst()) {
                clusterServiceOld.updateTaskAndOperationStatus(
                        task.id(),
                        TaskStatus.IN_PROGRESS,
                        null,
                        task.operationId(),
                        OperationStatus.IN_PROGRESS
                );
            }
            if (task.hasNoAttempts()) {
                throw new ClusterTaskHasNoAttemptsException(task.id());
            }

            TaskProcessingResult processingResult = taskProcessor.process(task);
            handleProcessingResult(task, processingResult);
        } catch (Exception e) {
            logger.error("Failed to process cluster task with id={}", task.id(), e);

            if (task.attemptsLeft() > 1) {
                clusterServiceNew.updateTask(
                        task.id(),
                        TaskStatus.SCHEDULED,
                        task.attemptsLeft() - 1,
                        task.getScheduledAtWithDelay(DEFAULT_SCHEDULED_AT_DELAY)
                );
            } else {
                clusterServiceOld.updateTaskAndOperationStatus(
                        task.id(),
                        TaskStatus.ERROR,
                        0,
                        task.operationId(),
                        OperationStatus.ERROR
                );
            }
        }
    }

    private void handleProcessingResult(Task task, TaskProcessingResult processingResult) {
        if (processingResult.status().equalsStringEnum(TaskStatus.SCHEDULED)) {
            logger.info("Rescheduling task with id={}", task.id());

            if (task.hasNoAttempts()) {
                throw new ClusterTaskHasNoAttemptsException(task.id());
            }

            clusterServiceNew.updateTask(
                    task.id(),
                    TaskStatus.SCHEDULED,
                    task.attemptsLeft() - 1,
                    processingResult.scheduledAt()
            );
            return;
        }

        if (task.isLast()) {
            clusterServiceOld.updateTaskAndOperationStatus(
                    task.id(),
                    TaskStatus.SUCCESS,
                    null,
                    task.operationId(),
                    OperationStatus.SUCCESS
            );
            return;
        }
        clusterServiceOld.updateTaskStatus(task.id(), TaskStatus.SUCCESS);
    }
}
