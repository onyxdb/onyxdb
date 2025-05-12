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

import com.onyxdb.platform.mdb.operations.OperationService;
import com.onyxdb.platform.mdb.operations.models.Operation;
import com.onyxdb.platform.mdb.operations.models.OperationStatus;
import com.onyxdb.platform.mdb.operations.producers.TaskProducerProvider;
import com.onyxdb.platform.mdb.operations.repositories.OperationRepository;
import com.onyxdb.platform.mdb.operations.repositories.TaskRepository;

@Component
@RequiredArgsConstructor
public class ProduceOperationTasksJob {
    private static final Logger logger = LoggerFactory.getLogger(ProduceOperationTasksJob.class);

    private final OperationRepository operationRepository;
    private final ObjectMapper objectMapper;
    private final TaskRepository taskRepository;
    private final TransactionTemplate transactionTemplate;
    private final TaskProducerProvider taskProducerProvider;
    private final OperationService operationService;

    @Scheduled(fixedRateString = "PT2S")
    @SchedulerLock(
            name = "ProduceOperationTasksTask",
            lockAtLeastFor = "1s",
            lockAtMostFor = "1s"
    )
    public void scheduledTask() {
        List<Operation> operations = operationRepository.listOperations(null, OperationStatus.SCHEDULED);

        for (Operation operation : operations) {
            logger.info("Registering operation id '{}'", operation.id());
            operationService.registerOperation(operation);
        }
    }
}
