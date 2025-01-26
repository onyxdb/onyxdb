package com.onyxdb.mdb.processors;

import org.springframework.stereotype.Component;

import com.onyxdb.mdb.models.ClusterOperation;
import com.onyxdb.mdb.models.ClusterTaskType;
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
//        ClusterTaskType clusterTaskType = clusterOperation.type();
//        switch (clusterTaskType) {
//            case M -> System.out.println("MongoDB Create Cluster");
//            case MONGODB_DELETE_CLUSTER_INIT -> System.out.println("MongoDB Delete Cluster");
//            default -> throw new IllegalStateException("Unexpected value: " + clusterTaskType);
//        }
    }

    private void finalizeOperation(ClusterOperation clusterOperation) {

    }
}
