package com.onyxdb.mdb.generators;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.onyxdb.mdb.exceptions.InternalServerErrorException;
import com.onyxdb.mdb.models.ClusterOperationType;
import com.onyxdb.mdb.models.ClusterTask;
import com.onyxdb.mdb.models.ClusterTaskType;
import com.onyxdb.mdb.models.ClusterType;

/**
 * @author foxleren
 */
@Service
public class MongoClusterTasksGenerator implements ClusterTasksGenerator {
    private static final ClusterType CLUSTER_TYPE = ClusterType.MONGODB;

    @Override
    public ClusterType getClusterType() {
        return CLUSTER_TYPE;
    }

    @Override
    public List<ClusterTask> generateTasks(UUID clusterId, UUID operationId, ClusterOperationType operationType) {
        switch (operationType) {
            case CREATE_CLUSTER -> {
                return getTasksForMongoCreateCluster(clusterId, operationId);
            }
            default -> throw new InternalServerErrorException(String.format(
                    "Can't generate tasks for operation type %s", operationType
            ));
        }
    }


    private static List<ClusterTask> getTasksForMongoCreateCluster(UUID clusterId, UUID operationId) {
        LocalDateTime now = LocalDateTime.now();
        var applyManifestTask = ClusterTask.scheduledNotLast(
                clusterId,
                operationId,
                CLUSTER_TYPE,
                ClusterTaskType.MONGODB_CREATE_CLUSTER_APPLY_MANIFEST,
                now,
                DEFAULT_RETRIES_LEFT,
                List.of()
        );
        var saveHostsTask = ClusterTask.scheduledNotLast(
                clusterId,
                operationId,
                CLUSTER_TYPE,
                ClusterTaskType.MONGODB_CREATE_CLUSTER_SAVE_HOSTS,
                now,
                DEFAULT_RETRIES_LEFT,
                List.of(applyManifestTask.id())
        );
        var generateGrafanaDashboardTask = ClusterTask.scheduledLast(
                clusterId,
                operationId,
                CLUSTER_TYPE,
                ClusterTaskType.MONGODB_CREATE_CLUSTER_GENERATE_GRAFANA_DASHBOARD,
                now,
                DEFAULT_RETRIES_LEFT,
                List.of(applyManifestTask.id(), saveHostsTask.id())
        );
        return List.of(
                applyManifestTask,
                saveHostsTask,
                generateGrafanaDashboardTask
        );
    }
}
