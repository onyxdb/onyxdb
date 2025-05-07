package com.onyxdb.platform.mdb.operations.consumers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.mdb.exceptions.InternalServerErrorException;
import com.onyxdb.platform.mdb.operations.OperationService;
import com.onyxdb.platform.mdb.operations.models.Task;
import com.onyxdb.platform.mdb.operations.models.TaskResult;
import com.onyxdb.platform.mdb.operations.models.TaskType;

/**
 * @author foxleren
 */
public class CompositeTaskConsumer {
    private static final Logger logger = LoggerFactory.getLogger(CompositeTaskConsumer.class);

    private final OperationService operationService;
    private final TransactionTemplate transactionTemplate;
    private final Map<TaskType, TaskConsumer<?>> taskTypeToTaskProcessors;

    public CompositeTaskConsumer(
            OperationService operationService,
            TransactionTemplate transactionTemplate,
            Map<TaskType, TaskConsumer<?>> taskTypeToTaskProcessors

    ) {
        this.operationService = operationService;
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

            Task startedTask = operationService.markTaskAsStarted(task);

            TaskResult taskResult = taskConsumer.process(startedTask);

            if (taskResult.ok()) {
                operationService.markTaskAsFinished(startedTask);
                return;
            }

            operationService.rescheduleTask(startedTask);
        } catch (Exception e) {
            logger.error("Failed to consume task with id '{}'", task.id(), e);
            operationService.rescheduleTask(task);
        }
    }
}
