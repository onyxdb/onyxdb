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

        var createClusterTask = ClusterTask.scheduledFirst(
                clusterId,
                operationId,
                CLUSTER_TYPE,
                ClusterTaskType.MONGODB_CREATE_CLUSTER,
                now,
                DEFAULT_RETRIES_LEFT
        );
        var checkClusterReadinessTask = ClusterTask.scheduledMiddle(
                clusterId,
                operationId,
                CLUSTER_TYPE,
                ClusterTaskType.MONGODB_CHECK_CLUSTER_READINESS,
                now,
                DEFAULT_RETRIES_LEFT
        );
        var createExporterServiceTask = ClusterTask.scheduledMiddle(
                clusterId,
                operationId,
                CLUSTER_TYPE,
                ClusterTaskType.MONGODB_CREATE_EXPORTER_SERVICE,
                now,
                DEFAULT_RETRIES_LEFT
        );
        var checkExporterReadinessTask = ClusterTask.scheduledMiddle(
                clusterId,
                operationId,
                CLUSTER_TYPE,
                ClusterTaskType.MONGODB_CHECK_EXPORTER_SERVICE_READINESS,
                now,
                DEFAULT_RETRIES_LEFT
        );
        var createExporterServiceScrape = ClusterTask.scheduledMiddle(
                clusterId,
                operationId,
                CLUSTER_TYPE,
                ClusterTaskType.MONGODB_CREATE_EXPORTER_SERVICE_SCRAPE,
                now,
                DEFAULT_RETRIES_LEFT
        );
        var createGrafanaDashboardTask = ClusterTask.scheduledLast(
                clusterId,
                operationId,
                CLUSTER_TYPE,
                ClusterTaskType.MONGODB_CHECK_EXPORTER_SERVICE_SCRAPE_READINESS,
                now,
                DEFAULT_RETRIES_LEFT
        );

        // TODO validate that there is first and last tasks and there is no duplicates.

        return List.of(
                ClusterTaskWithBlockers.withoutBlockers(createClusterTask),
                new ClusterTaskWithBlockers(
                        checkClusterReadinessTask,
                        List.of(createClusterTask.id())
                ),
                new ClusterTaskWithBlockers(
                        createExporterServiceTask,
                        List.of(
                                createClusterTask.id(),
                                checkClusterReadinessTask.id()
                        )
                ),
                new ClusterTaskWithBlockers(
                        checkExporterReadinessTask,
                        List.of(
                                createClusterTask.id(),
                                checkClusterReadinessTask.id(),
                                createExporterServiceTask.id()
                        )
                ),
                new ClusterTaskWithBlockers(
                        createExporterServiceScrape,
                        List.of(
                                createClusterTask.id(),
                                checkClusterReadinessTask.id(),
                                createExporterServiceTask.id(),
                                checkExporterReadinessTask.id()
                        )
                ),
                new ClusterTaskWithBlockers(
                        createGrafanaDashboardTask,
                        List.of(
                                createClusterTask.id(),
                                checkClusterReadinessTask.id(),
                                createExporterServiceTask.id(),
                                checkExporterReadinessTask.id(),
                                createExporterServiceScrape.id()
                        )
                )
        );
    }
}
