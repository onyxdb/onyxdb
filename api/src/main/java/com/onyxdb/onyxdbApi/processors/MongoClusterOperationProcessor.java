package com.onyxdb.onyxdbApi.processors;

import com.onyxdb.onyxdbApi.models.ClusterOperation;
import com.onyxdb.onyxdbApi.models.ClusterType;

/**
 * @author foxleren
 */
public class MongoClusterOperationProcessor implements ClusterOperationProcessor {
    @Override
    public ClusterType getClusterType() {
        return ClusterType.MONGODB;
    }

    @Override
    public void process(ClusterOperation clusterOperation) {
        throw new RuntimeException("Not implemented");
    }
}
