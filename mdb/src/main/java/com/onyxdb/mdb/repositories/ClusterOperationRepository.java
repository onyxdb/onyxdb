package com.onyxdb.mdb.repositories;

import java.util.List;

import com.onyxdb.mdb.models.ClusterOperation;

/**
 * @author foxleren
 */
public interface ClusterOperationRepository {
    void createBulk(List<ClusterOperation> clusterOperations);
}
