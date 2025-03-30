package com.onyxdb.mdb.repositories;

import java.util.UUID;

import com.onyxdb.mdb.core.clusters.models.ClusterOperation;
import com.onyxdb.mdb.core.clusters.models.ClusterOperationStatus;

/**
 * @author foxleren
 */
public interface ClusterOperationRepository {
    void create(ClusterOperation operation);

    void updateStatus(UUID id, ClusterOperationStatus status);
}
