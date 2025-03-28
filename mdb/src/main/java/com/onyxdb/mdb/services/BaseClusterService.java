package com.onyxdb.mdb.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.mdb.core.clusters.models.Cluster;
import com.onyxdb.mdb.models.ClusterOperationStatus;
import com.onyxdb.mdb.models.ClusterTask;
import com.onyxdb.mdb.models.ClusterTaskStatus;
import com.onyxdb.mdb.models.ClusterToCreate;

/**
 * @author foxleren
 */
public interface BaseClusterService {
    UUID create(UUID createdBy, ClusterToCreate clusterToCreate);

    Optional<Cluster> getByIdO(UUID id);

    void updateTaskStatus(UUID taskId, ClusterTaskStatus taskStatus);

    void updateOperationStatus(UUID operationId, ClusterOperationStatus operationStatus);

    void updateTaskAndOperationStatus(
            UUID taskId,
            ClusterTaskStatus taskStatus,
            UUID operationId,
            ClusterOperationStatus operationStatus
    );

    List<ClusterTask> getTasksToProcess(int limit);

    void updateProject(UUID clusterId, UUID projectId);
}
