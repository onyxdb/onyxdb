package com.onyxdb.mdb.repositories;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.onyxdb.mdb.models.ClusterTask;
import com.onyxdb.mdb.models.ClusterTaskStatus;

/**
 * @author foxleren
 */
public interface ClusterTaskRepository {
    void createBulk(List<ClusterTask> tasks);

    void createBlockerTasksBulk(Map<UUID, List<UUID>> taskIdToBlockingTaskIds);

    List<ClusterTask> getTasksToProcess(int limit);

    void updateStatus(UUID id, ClusterTaskStatus status);
}
