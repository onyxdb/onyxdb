package com.onyxdb.mdb.processors;

import org.springframework.stereotype.Component;

import com.onyxdb.mdb.models.ClusterOperation;
import com.onyxdb.mdb.models.ClusterOperationType;
import com.onyxdb.mdb.models.ClusterType;

/**
 * @author foxleren
 */
@Component
public class MongoClusterOperationProcessor implements ClusterOperationProcessor {
    @Override
    public ClusterType getClusterType() {
        return ClusterType.MONGODB;
    }

    @Override
    public void process(ClusterOperation clusterOperation) {
        ClusterOperationType clusterOperationType = clusterOperation.type();
        switch (clusterOperationType) {
            case MONGODB_CREATE_CLUSTER -> System.out.println("MongoDB Create Cluster");
            case MONGODB_DELETE_CLUSTER -> System.out.println("MongoDB Delete Cluster");
            default -> throw new IllegalStateException("Unexpected value: " + clusterOperationType);
        }
    }
}
