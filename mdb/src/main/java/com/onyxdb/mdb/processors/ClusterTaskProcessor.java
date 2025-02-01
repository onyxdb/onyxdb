package com.onyxdb.mdb.processors;

import com.onyxdb.mdb.models.ClusterTask;
import com.onyxdb.mdb.models.ClusterType;

/**
 * @author foxleren
 */
public interface ClusterTaskProcessor {
    ClusterType getClusterType();

    void process(ClusterTask task);
}
