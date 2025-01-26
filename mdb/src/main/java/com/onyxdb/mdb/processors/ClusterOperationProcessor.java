package com.onyxdb.mdb.processors;

import com.onyxdb.mdb.models.ClusterOperation;
import com.onyxdb.mdb.models.ClusterType;

/**
 * @author foxleren
 */
public interface ClusterOperationProcessor {
    ClusterType getClusterType();

    void process(ClusterOperation clusterOperation);
}
