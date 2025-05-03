package com.onyxdb.platform.mdb.operations.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.onyxdb.platform.mdb.operations.models.Operation;
import com.onyxdb.platform.mdb.operations.models.OperationStatus;

/**
 * @author foxleren
 */
public interface OperationRepository {
    void createOperation(Operation operation);

    void updateStatus(UUID id, OperationStatus status);

    List<Operation> listOperations(@Nullable UUID clusterId, @Nullable OperationStatus status);

    Optional<Operation> getForUpdateO(UUID operationId);

    Operation getOperation(UUID operationId);
}
