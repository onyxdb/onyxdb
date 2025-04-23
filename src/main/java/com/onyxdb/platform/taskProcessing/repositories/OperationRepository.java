package com.onyxdb.platform.taskProcessing.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.platform.taskProcessing.models.Operation;
import com.onyxdb.platform.taskProcessing.models.OperationStatus;

/**
 * @author foxleren
 */
public interface OperationRepository {
    void create(Operation operation);

    void updateStatus(UUID id, OperationStatus status);

    List<Operation> listOperations(UUID clusterId);

    Optional<Operation> getForUpdateO(UUID operationId);
}
