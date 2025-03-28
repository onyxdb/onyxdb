package com.onyxdb.mdb.processors;

import org.springframework.stereotype.Component;

import com.onyxdb.mdb.core.clusters.models.ClusterType;
import com.onyxdb.mdb.exceptions.NotImplementedClusterTaskTypeException;
import com.onyxdb.mdb.models.ClusterTask;
import com.onyxdb.mdb.models.ClusterTaskType;

/**
 * @author foxleren
 */
@Component
public class MongoClusterTaskProcessor implements ClusterTaskProcessor {
    @Override
    public ClusterType getClusterType() {
        return ClusterType.MONGODB;
    }

    @Override
    public void process(ClusterTask task) {
        ClusterTaskType clusterTaskType = task.type();
        switch (clusterTaskType) {
            case MONGODB_CREATE_CLUSTER_APPLY_MANIFEST -> System.out.println("MONGODB_CREATE_CLUSTER_APPLY_MANIFEST");
            case MONGODB_CREATE_CLUSTER_SAVE_HOSTS -> System.out.println("MONGODB_CREATE_CLUSTER_SAVE_HOSTS");
            case MONGODB_CREATE_CLUSTER_GENERATE_GRAFANA_DASHBOARD -> System.out.println("MONGODB_CREATE_CLUSTER_GENERATE_GRAFANA_DASHBOARD");
            default -> throw new NotImplementedClusterTaskTypeException(task.type());
        }
    }
}
