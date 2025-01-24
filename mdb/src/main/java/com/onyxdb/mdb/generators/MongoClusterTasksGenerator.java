package com.onyxdb.mdb.generators;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;
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
public class MongoClusterTasksGenerator extends ClusterTasksGenerator {
    private static final int DEFAULT_MAX_RETRIES = 3;

    private static final ClusterType CLUSTER_TYPE = ClusterType.MONGODB;

    @Override
    public ClusterType getClusterType() {
        return ClusterType.MONGODB;
    }

    @Override
    public List<ClusterTask> generateTasks(UUID clusterId, ClusterOperationType operationType) {
        switch (operationType) {
            case MONGODB_CREATE_CLUSTER -> {
                return getTasksForMongoCreateCluster(clusterId);
            }
            case MONGODB_DELETE_CLUSTER -> {
                return getTasksForMongoDeleteCluster();
            }
            default -> throw new InternalServerErrorException(String.format(
                    "Can't generate tasks for operation type %s", operationType
            ));
        }
    }


    private static List<ClusterTask> getTasksForMongoCreateCluster(UUID clusterId) {
        LocalDateTime now = LocalDateTime.now();
        var applyManifestTask = ClusterTask.createNotLast(
                ClusterTaskType.MONGODB_CREATE_CLUSTER_APPLY_CLUSTER_MANIFEST,
                clusterId,
                CLUSTER_TYPE,
                now,
                DEFAULT_MAX_RETRIES,
                List.of()
        );
        var waitReadinessTask = ClusterTask.createNotLast(
                ClusterTaskType.MONGODB_CREATE_CLUSTER_CHECK_CLUSTER_READINESS,
                clusterId,
                CLUSTER_TYPE,
                now.plusSeconds(300),
                DEFAULT_MAX_RETRIES,
                List.of(applyManifestTask.id())
        );
        var generateDashboardTask = ClusterTask.createLast(
                ClusterTaskType.MONGODB_CREATE_CLUSTER_GENERATE_GRAFANA_DASHBOARD,
                clusterId,
                CLUSTER_TYPE,
                now.plusSeconds(300),
                DEFAULT_MAX_RETRIES,
                List.of(applyManifestTask.id(), waitReadinessTask.id())
        );
        return List.of(
                applyManifestTask,
                waitReadinessTask,
                generateDashboardTask
        );
    }

    private static List<ClusterTask> getTasksForMongoDeleteCluster() {
        return List.of();
    }
}
