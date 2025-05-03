package com.onyxdb.platform.mdb.jobs;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.mdb.scheduling.operations.OperationRepository;
import com.onyxdb.platform.mdb.scheduling.operations.models.Operation;
import com.onyxdb.platform.mdb.scheduling.operations.models.OperationStatus;
import com.onyxdb.platform.mdb.scheduling.tasks.TaskRepository;
import com.onyxdb.platform.mdb.scheduling.tasks.TaskScheduler;
import com.onyxdb.platform.mdb.scheduling.tasks.producers.TaskProducerProvider;

@Component
@RequiredArgsConstructor
public class ProduceOperationTasksJob {
    private static final Logger logger = LoggerFactory.getLogger(ProduceOperationTasksJob.class);

    private final OperationRepository operationRepository;
    private final ObjectMapper objectMapper;
    private final TaskRepository taskRepository;
    private final TransactionTemplate transactionTemplate;
    private final TaskProducerProvider taskProducerProvider;
    private final TaskScheduler taskScheduler;

    @Scheduled(fixedRateString = "PT2S")
    @SchedulerLock(
            name = "ProduceOperationTasksTask",
            lockAtLeastFor = "1s",
            lockAtMostFor = "1s"
    )
    public void scheduledTask() {
        List<Operation> operations = operationRepository.listOperations(null, OperationStatus.SCHEDULED);
        logger.info("Scheduled operations for {}", operations.size());

        for (Operation operation : operations) {
            taskScheduler.registerOperation(operation);
        }
    }
}
