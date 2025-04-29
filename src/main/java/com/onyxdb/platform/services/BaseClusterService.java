package com.onyxdb.platform.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.onyxdb.platform.core.clusters.models.Cluster;
import com.onyxdb.platform.processing.models.OperationStatus;
import com.onyxdb.platform.processing.models.Task;
import com.onyxdb.platform.processing.models.TaskStatus;

/**
 * @author foxleren
 */
public interface BaseClusterService {
    Optional<Cluster> getByIdO(UUID id);

    void updateTaskStatus(UUID taskId, TaskStatus taskStatus);

    void updateOperationStatus(UUID operationId, OperationStatus operationStatus);

    void updateTaskAndOperationStatus(
            UUID taskId,
            TaskStatus taskStatus,
            @Nullable
            Integer attemptsLeft,
            UUID operationId,
            OperationStatus operationStatus
    );

    List<Task> getTasksToProcess(int limit, LocalDateTime scheduledAt);

    void updateProject(UUID clusterId, UUID projectId);
}
