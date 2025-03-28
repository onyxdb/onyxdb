package com.onyxdb.mdb.generators;

import java.util.List;
import java.util.UUID;

import com.onyxdb.mdb.core.clusters.models.ClusterType;
import com.onyxdb.mdb.models.ClusterOperationType;
import com.onyxdb.mdb.models.ClusterTaskWithBlockers;

/**
 * @author foxleren
 */
public interface ClusterTasksGenerator {
    int DEFAULT_RETRIES_LEFT = 3;

    ClusterType getClusterType();

    List<ClusterTaskWithBlockers> generateTasks(UUID clusterId, UUID operationId, ClusterOperationType operationType);
}
