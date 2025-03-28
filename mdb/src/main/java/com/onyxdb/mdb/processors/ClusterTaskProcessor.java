package com.onyxdb.mdb.processors;

import com.onyxdb.mdb.core.clusters.models.ClusterType;
import com.onyxdb.mdb.models.ClusterTask;

/**
 * @author foxleren
 */
public interface ClusterTaskProcessor {
    ClusterType getClusterType();

    void process(ClusterTask task);
}
