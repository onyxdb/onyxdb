package com.onyxdb.mdb.generators;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.onyxdb.mdb.models.ClusterOperationType;
import com.onyxdb.mdb.models.ClusterTask;
import com.onyxdb.mdb.models.ClusterType;

/**
 * @author foxleren
 */
public abstract class ClusterTasksGenerator {
    public abstract ClusterType getClusterType();

    public abstract List<ClusterTask> generateTasks(UUID clusterId, ClusterOperationType operationType);

    private LocalDateTime calculateExecuteAt() {
        return LocalDateTime.now();
    }
}
