package com.onyxdb.platform.mdb.scheduling.tasks.consumers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.mdb.exceptions.InternalServerErrorException;
import com.onyxdb.platform.mdb.scheduling.operations.OperationService;
import com.onyxdb.platform.mdb.scheduling.tasks.TaskScheduler;
import com.onyxdb.platform.mdb.scheduling.tasks.models.Task;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskResult;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskType;

/**
 * @author foxleren
 */
public class CompositeTaskConsumer {
    private static final Logger logger = LoggerFactory.getLogger(CompositeTaskConsumer.class);

    private final TaskScheduler taskScheduler;
    private final TransactionTemplate transactionTemplate;
    private final Map<TaskType, TaskConsumer<?>> taskTypeToTaskProcessors;

    public CompositeTaskConsumer(
            OperationService operationService,
            TaskScheduler taskScheduler,
            TransactionTemplate transactionTemplate,
            Map<TaskType, TaskConsumer<?>> taskTypeToTaskProcessors

    ) {
        this.taskScheduler = taskScheduler;
        this.transactionTemplate = transactionTemplate;
        this.taskTypeToTaskProcessors = taskTypeToTaskProcessors;
    }

    public void processTask(Task task) {
        try {
            if (!taskTypeToTaskProcessors.containsKey(task.type())) {
                throw new InternalServerErrorException(String.format(
                        "Can't find consumer for task type '%s'", task.type()
                ));
            }
            TaskConsumer<?> taskConsumer = taskTypeToTaskProcessors.get(task.type());

            taskScheduler.markTaskAsStarted(task);

            TaskResult taskResult = taskConsumer.process(task);

            if (taskResult.ok()) {
                taskScheduler.markTaskAsFinished(task);
                return;
            }

            // TODO recalculate scheduledAt for this and further tasks
            taskScheduler.rescheduleTask(task);
        } catch (Exception e) {
            logger.error("Failed to consume task with id '{}'", task.id(), e);
            taskScheduler.rescheduleTask(task);
        }
    }
}
