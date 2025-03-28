package com.onyxdb.mdb.generators;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.onyxdb.mdb.core.clusters.models.ClusterType;
import com.onyxdb.mdb.exceptions.InternalServerErrorException;
import com.onyxdb.mdb.models.ClusterOperationType;
import com.onyxdb.mdb.models.ClusterTask;
import com.onyxdb.mdb.models.ClusterTaskType;
import com.onyxdb.mdb.models.ClusterTaskWithBlockers;

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
    public List<ClusterTaskWithBlockers> generateTasks(
            UUID clusterId,
            UUID operationId,
            ClusterOperationType operationType
    ) {
        switch (operationType) {
            case CREATE_CLUSTER -> {
                return getTasksForMongoCreateCluster(clusterId, operationId);
            }
            default -> throw new InternalServerErrorException(String.format(
                    "Can't generate tasks for operation type %s", operationType
            ));
        }
    }


    private static List<ClusterTaskWithBlockers> getTasksForMongoCreateCluster(UUID clusterId, UUID operationId) {
        LocalDateTime now = LocalDateTime.now();

        var applyManifestTask = ClusterTask.scheduledFirst(
                clusterId,
                operationId,
                CLUSTER_TYPE,
                ClusterTaskType.MONGODB_CREATE_CLUSTER_APPLY_MANIFEST,
                now,
                DEFAULT_RETRIES_LEFT
        );
        var saveHostsTask = ClusterTask.scheduledMiddle(
                clusterId,
                operationId,
                CLUSTER_TYPE,
                ClusterTaskType.MONGODB_CREATE_CLUSTER_SAVE_HOSTS,
                now,
                DEFAULT_RETRIES_LEFT
        );
        var generateGrafanaDashboardTask = ClusterTask.scheduledLast(
                clusterId,
                operationId,
                CLUSTER_TYPE,
                ClusterTaskType.MONGODB_CREATE_CLUSTER_GENERATE_GRAFANA_DASHBOARD,
                now,
                DEFAULT_RETRIES_LEFT
        );

        return List.of(
                ClusterTaskWithBlockers.withoutBlockers(applyManifestTask),
                new ClusterTaskWithBlockers(
                        saveHostsTask,
                        List.of(applyManifestTask.id())
                ),
                new ClusterTaskWithBlockers(
                        generateGrafanaDashboardTask,
                        List.of(applyManifestTask.id(), saveHostsTask.id())
                )
        );
    }
}
