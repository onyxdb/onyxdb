package com.onyxdb.mdb.core.clusters.generators;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.onyxdb.mdb.core.clusters.models.ClusterOperationType;
import com.onyxdb.mdb.core.clusters.models.ClusterTask;
import com.onyxdb.mdb.core.clusters.models.ClusterTaskType;
import com.onyxdb.mdb.core.clusters.models.ClusterTaskWithBlockers;
import com.onyxdb.mdb.core.clusters.models.ClusterType;
import com.onyxdb.mdb.exceptions.InternalServerErrorException;

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
                ClusterTaskType.APPLY_MANIFEST,
                now,
                DEFAULT_RETRIES_LEFT
        );
        var checkReadinessTask = ClusterTask.scheduledLast(
                clusterId,
                operationId,
                CLUSTER_TYPE,
                ClusterTaskType.CHECK_READINESS,
                now,
                DEFAULT_RETRIES_LEFT
        );

        return List.of(
                ClusterTaskWithBlockers.withoutBlockers(applyManifestTask),
                new ClusterTaskWithBlockers(
                        checkReadinessTask,
                        List.of(applyManifestTask.id())
                )
        );
    }
}
