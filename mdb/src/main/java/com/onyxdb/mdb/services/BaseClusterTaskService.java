package com.onyxdb.mdb.services;

import java.util.List;

import com.onyxdb.mdb.models.ClusterTask;

/**
 * @author foxleren
 */
public interface BaseClusterTaskService {
    List<ClusterTask> getTasksToProcess(int limit);
}
