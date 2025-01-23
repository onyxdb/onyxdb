package com.onyxdb.mdb.processors;

import com.onyxdb.mdb.models.ClusterType;
import com.onyxdb.mdb.models.ClusterOperation;

/**
 * @author foxleren
 */
public interface ClusterOperationProcessor {
    ClusterType getClusterType();

    void process(ClusterOperation clusterOperation);
}
