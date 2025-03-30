package com.onyxdb.mdb.core.clusters.processors;

import com.onyxdb.mdb.core.clusters.models.ClusterTask;
import com.onyxdb.mdb.core.clusters.models.ClusterType;

/**
 * @author foxleren
 */
public interface ClusterTaskProcessor {
    ClusterType getClusterType();

    ClusterTaskProcessingResult process(ClusterTask task);
}
