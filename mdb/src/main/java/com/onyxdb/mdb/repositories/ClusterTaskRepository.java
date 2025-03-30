package com.onyxdb.mdb.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.onyxdb.mdb.core.clusters.models.ClusterTask;
import com.onyxdb.mdb.core.clusters.models.ClusterTaskStatus;

/**
 * @author foxleren
 */
public interface ClusterTaskRepository {
    void createBulk(List<ClusterTask> tasks);

    void createBlockerTasksBulk(Map<UUID, List<UUID>> taskIdToBlockingTaskIds);

    List<ClusterTask> getTasksToProcess(
            int limit,
            LocalDateTime scheduledAt
    );

    void updateStatus(UUID id, ClusterTaskStatus status);

    void updateTask(
            UUID id,
            ClusterTaskStatus status,
            @Nullable
            Integer attemptsLeft,
            @Nullable
            LocalDateTime scheduledAt
    );
}
