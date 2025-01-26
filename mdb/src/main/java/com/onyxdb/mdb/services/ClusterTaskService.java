package com.onyxdb.mdb.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.onyxdb.mdb.models.ClusterTask;

/**
 * @author foxleren
 */
@Service
public class ClusterTaskService implements BaseClusterTaskService {
    @Override
    public List<ClusterTask> getTasksToProcess(int limit) {
        return List.of();
    }
}
