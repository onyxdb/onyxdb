package com.onyxdb.mdb.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.onyxdb.mdb.core.clusters.models.Cluster;
import com.onyxdb.mdb.core.clusters.models.ClusterOperationStatus;
import com.onyxdb.mdb.core.clusters.models.ClusterTask;
import com.onyxdb.mdb.core.clusters.models.ClusterTaskStatus;

/**
 * @author foxleren
 */
public interface BaseClusterService {
    Optional<Cluster> getByIdO(UUID id);

    void updateTaskStatus(UUID taskId, ClusterTaskStatus taskStatus);

    void updateOperationStatus(UUID operationId, ClusterOperationStatus operationStatus);

    void updateTaskAndOperationStatus(
            UUID taskId,
            ClusterTaskStatus taskStatus,
            @Nullable
            Integer attemptsLeft,
            UUID operationId,
            ClusterOperationStatus operationStatus
    );

    List<ClusterTask> getTasksToProcess(int limit, LocalDateTime scheduledAt);

    void updateProject(UUID clusterId, UUID projectId);
}
