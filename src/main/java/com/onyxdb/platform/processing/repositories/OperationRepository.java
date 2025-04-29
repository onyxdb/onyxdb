package com.onyxdb.platform.processing.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.onyxdb.platform.processing.models.Operation;
import com.onyxdb.platform.processing.models.OperationStatus;

/**
 * @author foxleren
 */
public interface OperationRepository {
    void createOperation(Operation operation);

    void updateStatus(UUID id, OperationStatus status);

    List<Operation> listOperations(@Nullable UUID clusterId, @Nullable OperationStatus status);

    Optional<Operation> getForUpdateO(UUID operationId);
}
