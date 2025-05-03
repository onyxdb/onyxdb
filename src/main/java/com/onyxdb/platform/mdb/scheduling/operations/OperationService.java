package com.onyxdb.platform.mdb.scheduling.operations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.mdb.exceptions.OperationNotFoundException;
import com.onyxdb.platform.mdb.exceptions.OperationRestartNotAllowedException;
import com.onyxdb.platform.mdb.scheduling.operations.models.Operation;
import com.onyxdb.platform.mdb.scheduling.operations.models.OperationStatus;
import com.onyxdb.platform.mdb.scheduling.tasks.TaskRepository;
import com.onyxdb.platform.mdb.scheduling.tasks.models.Task;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskFilter;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskStatus;

public class OperationService {
    private final OperationRepository operationRepository;
    private final TaskRepository taskRepository;
    private final TransactionTemplate transactionTemplate;

    public OperationService(
            OperationRepository operationRepository,
            TaskRepository taskRepository,
            TransactionTemplate transactionTemplate
    ) {
        this.operationRepository = operationRepository;
        this.taskRepository = taskRepository;
        this.transactionTemplate = transactionTemplate;
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
            List<Task> scheduledTasks = failedTasks.stream().map(failedTask -> Task.builder()
                    .copy(failedTask)
                    .withStatus(TaskStatus.SCHEDULED)
                    //TODO get needed attempts from single place for generating and restarting
                    .withAttemptsLeft(5)
                    .build()
            ).toList();

            taskRepository.updateTasks(scheduledTasks);
            operationRepository.updateStatus(operationId, OperationStatus.IN_PROGRESS);
        });
    }

    public void markOperationAsFailed(UUID operationId) {
        operationRepository.updateStatus(operationId, OperationStatus.ERROR);
    }
}
