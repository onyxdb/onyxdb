package com.onyxdb.mdb.taskProcessing.repositories;

import java.util.UUID;

import com.onyxdb.mdb.taskProcessing.models.Operation;
import com.onyxdb.mdb.taskProcessing.models.OperationStatus;

/**
 * @author foxleren
 */
public interface OperationRepository {
    void create(Operation operation);

    void updateStatus(UUID id, OperationStatus status);
}
