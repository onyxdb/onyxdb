package com.onyxdb.mdb.repositories;

import java.util.List;
import java.util.UUID;

import com.onyxdb.mdb.models.ClusterTask;

/**
 * @author foxleren
 */
public interface ClusterTaskRepository {
    void createBulk(List<ClusterTask> clusterTasks);

    List<ClusterTask> getTasksToProcess(int limit);
}
