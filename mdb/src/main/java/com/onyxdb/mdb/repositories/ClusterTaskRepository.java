package com.onyxdb.mdb.repositories;

import java.util.List;

import com.onyxdb.mdb.models.ClusterTask;

/**
 * @author foxleren
 */
public interface ClusterTaskRepository {
    void createBulk(List<ClusterTask> clusterTasks);
}
