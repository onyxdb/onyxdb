package com.onyxdb.platform.tasks;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.operationsOLD.tasks.ProducedTask;
import com.onyxdb.platform.processing.TaskProcessingUtils;
import com.onyxdb.platform.processing.consumers.mongo.MongoCreateBackupTaskProcessor;
import com.onyxdb.platform.processing.models.Operation;
import com.onyxdb.platform.processing.models.OperationStatus;
import com.onyxdb.platform.processing.models.Task;
import com.onyxdb.platform.processing.models.TaskStatus;
import com.onyxdb.platform.processing.models.TaskType;
import com.onyxdb.platform.processing.models.TaskWithBlockers;
import com.onyxdb.platform.processing.producers.mongo.MongoCreateBackupTaskProducer;
import com.onyxdb.platform.processing.producers.mongo.MongoCreateClusterTaskProducer;
import com.onyxdb.platform.processing.producers.mongo.MongoCreateDatabaseTaskProducer;
import com.onyxdb.platform.processing.producers.mongo.MongoCreateUserTaskProducer;
import com.onyxdb.platform.processing.producers.mongo.MongoDeleteClusterTaskGenerator;
import com.onyxdb.platform.processing.producers.mongo.MongoDeleteDatabaseTaskProducer;
import com.onyxdb.platform.processing.producers.mongo.MongoDeleteUserTaskProducer;
import com.onyxdb.platform.processing.producers.mongo.MongoScaleClusterTaskProducer;
import com.onyxdb.platform.processing.repositories.OperationRepository;
import com.onyxdb.platform.processing.repositories.TaskRepository;
import com.onyxdb.platform.utils.ObjectMapperUtils;
import com.onyxdb.platform.utils.TimeUtils;

@Component
@RequiredArgsConstructor
public class ProduceOperationTasksTask {
    private static final Logger logger = LoggerFactory.getLogger(ProduceOperationTasksTask.class);

    private final OperationRepository operationRepository;
    private final ObjectMapper objectMapper;
    private final TaskRepository taskRepository;
    private final TransactionTemplate transactionTemplate;

    private final MongoCreateClusterTaskProducer mongoCreateClusterTaskProducer;
    private final MongoDeleteClusterTaskGenerator mongoDeleteClusterTaskGenerator;
    private final MongoScaleClusterTaskProducer mongoScaleClusterTaskProducer;
    private final MongoCreateDatabaseTaskProducer mongoCreateDatabaseTaskProducer;
    private final MongoDeleteDatabaseTaskProducer mongoDeleteDatabaseTaskProducer;
    private final MongoCreateUserTaskProducer mongoCreateUserTaskProducer;
    private final MongoDeleteUserTaskProducer mongoDeleteUserTaskProducer;
    private final MongoCreateBackupTaskProducer mongoCreateBackupTaskProducer;

    @Scheduled(fixedRateString = "PT2S")
    @SchedulerLock(
            name = "ProduceOperationTasksTask",
            lockAtLeastFor = "1s",
            lockAtMostFor = "1s"
    )
    public void scheduledTask() {
        List<Operation> operations = operationRepository.listOperations(null, OperationStatus.SCHEDULED);
        logger.info("Scheduled operations for {}", operations.size());

        List<TaskWithBlockers> allTasksWithBlockers = new ArrayList<>();
        for (Operation operation : operations) {
            List<ProducedTask> producedTasks = handleOperation(operation);
            logger.info("Produced tasks: {}", producedTasks);
            List<TaskWithBlockers> taskWithBlockers = prepareProducedTasks(producedTasks);

            allTasksWithBlockers.addAll(taskWithBlockers);
        }

        transactionTemplate.executeWithoutResult(status -> {
            List<Task> tasks = TaskProcessingUtils.getTasksFromTasksWithBlockers(allTasksWithBlockers);
            taskRepository.createBulk(tasks);
            taskRepository.createBlockerTasksBulk(allTasksWithBlockers);
            for (var operation : operations) {
                operationRepository.updateStatus(operation.id(), OperationStatus.IN_PROGRESS);
            }
        });
    }

    private List<ProducedTask> handleOperation(Operation operation) {
        switch (operation.type()) {
            case MONGO_CREATE_CLUSTER -> {
                return mongoCreateClusterTaskProducer.produceTasks(operation, operation.payload());
            }
            case MONGO_DELETE_CLUSTER -> {
                return mongoDeleteClusterTaskGenerator.produceTasks(operation, operation.payload());
            }
            case MONGO_SCALE_CLUSTER -> {
                return mongoScaleClusterTaskProducer.produceTasks(operation, operation.payload());
            }
            case MONGO_CREATE_DATABASE -> {
                return mongoCreateDatabaseTaskProducer.produceTasks(operation, operation.payload());
            }
            case MONGO_DELETE_DATABASE -> {
                return mongoDeleteDatabaseTaskProducer.produceTasks(operation, operation.payload());
            }
            case MONGO_CREATE_USER -> {
                return mongoCreateUserTaskProducer.produceTasks(operation, operation.payload());
            }
            case MONGO_DELETE_USER -> {
                return mongoDeleteUserTaskProducer.produceTasks(operation, operation.payload());
            }
            case MONGO_CREATE_BACKUP -> {
                return mongoCreateBackupTaskProducer.produceTasks(operation, operation.payload());
            }
            default -> throw new IllegalStateException("Unexpected value: " + operation.type());
        }
    }

    protected List<TaskWithBlockers> prepareProducedTasks(List<ProducedTask> producedTasks) {
        List<TaskWithBlockers> tasks = new ArrayList<>();

        for (int i = 0; i < producedTasks.size(); i++) {
            int attempts = 5;
            int delayAfterPreviousSeconds = 1;

            var prev = i > 0 ? tasks.get(i - 1) : null;
            var current = producedTasks.get(i);

            if (prev != null) {
                if (prev.task().type().equalsStringEnum(TaskType.MONGO_APPLY_PSMDB)) {
                    attempts = 10;
                    delayAfterPreviousSeconds = 60;
                    // 150 sec
                }
                if (prev.task().type().equalsStringEnum(TaskType.MONGO_APPLY_ONYXDB_AGENT)) {
                    delayAfterPreviousSeconds = 60;
                    attempts = 5;
                    // 50 sec
                }
                if (prev.task().type().equalsStringEnum(TaskType.MONGO_CREATE_DATABASE)) {
                    delayAfterPreviousSeconds = 30;
                    attempts = 5;
                    // 50 sec
                }

                if (current.type().equalsStringEnum(TaskType.MONGO_CREATE_USER)) {
                    attempts = 1;
                }
                if (current.type().equalsStringEnum(TaskType.MONGO_CREATE_DATABASE)) {
                    attempts = 1;
                }
            }

            var scheduledAt = prev == null ? TimeUtils.now() : prev.task().getScheduledAtWithDelay(
                    Duration.ofSeconds(delayAfterPreviousSeconds)
            );

            tasks.add(new TaskWithBlockers(
                    new Task(
                            current.id(),
                            current.type(),
                            TaskStatus.SCHEDULED,
                            current.operationId(),
                            TimeUtils.now(),
                            TimeUtils.now(),
                            scheduledAt,
                            attempts,
                            ObjectMapperUtils.convertToString(objectMapper, current.payload())
                    ),
                    current.blockerIds()
            ));
        }
        return tasks;
    }
}
