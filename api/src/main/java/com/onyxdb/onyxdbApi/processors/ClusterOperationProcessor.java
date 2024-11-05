package com.onyxdb.onyxdbApi.processors;

import com.onyxdb.onyxdbApi.models.ClusterOperation;
import com.onyxdb.onyxdbApi.models.ClusterType;

/**
 * @author foxleren
 */
public interface ClusterOperationProcessor {
    ClusterType getClusterType();

    void process(ClusterOperation clusterOperation);
}
