package com.onyxdb.mdb.core.clusters.generators;

import java.util.List;
import java.util.UUID;

import com.onyxdb.mdb.core.clusters.models.ClusterOperationType;
import com.onyxdb.mdb.core.clusters.models.ClusterTaskWithBlockers;
import com.onyxdb.mdb.core.clusters.models.ClusterType;

/**
 * @author foxleren
 */
public interface ClusterTasksGenerator {
    int DEFAULT_RETRIES_LEFT = 3;

    ClusterType getClusterType();

    List<ClusterTaskWithBlockers> generateTasks(UUID clusterId, UUID operationId, ClusterOperationType operationType);
}
