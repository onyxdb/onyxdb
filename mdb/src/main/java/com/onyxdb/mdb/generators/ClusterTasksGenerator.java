package com.onyxdb.mdb.generators;

import java.util.List;
import java.util.UUID;

import com.onyxdb.mdb.models.ClusterOperationType;
import com.onyxdb.mdb.models.ClusterTask;
import com.onyxdb.mdb.models.ClusterType;

/**
 * @author foxleren
 */
public interface ClusterTasksGenerator {
    int DEFAULT_RETRIES_LEFT = 3;

    ClusterType getClusterType();

    List<ClusterTask> generateTasks(UUID clusterId, UUID operationId, ClusterOperationType operationType);
}
