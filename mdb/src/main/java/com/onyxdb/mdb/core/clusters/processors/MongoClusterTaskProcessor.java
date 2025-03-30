package com.onyxdb.mdb.core.clusters.processors;

import java.time.Duration;

import org.springframework.stereotype.Component;

import com.onyxdb.mdb.clients.psmdb.PsmdbClient;
import com.onyxdb.mdb.core.clusters.ClusterService;
import com.onyxdb.mdb.core.clusters.models.Cluster;
import com.onyxdb.mdb.core.clusters.models.ClusterTask;
import com.onyxdb.mdb.core.clusters.models.ClusterTaskType;
import com.onyxdb.mdb.core.clusters.models.ClusterType;
import com.onyxdb.mdb.exceptions.NotImplementedClusterTaskTypeException;

/**
 * @author foxleren
 */
@Component
public class MongoClusterTaskProcessor implements ClusterTaskProcessor {
    private final PsmdbClient psmdbClient;
    private final ClusterService clusterService;

    public MongoClusterTaskProcessor(
            PsmdbClient psmdbClient,
            ClusterService clusterService
    ) {
        this.psmdbClient = psmdbClient;
        this.clusterService = clusterService;
    }

    @Override
    public ClusterType getClusterType() {
        return ClusterType.MONGODB;
    }

    @Override
    public ClusterTaskProcessingResult process(ClusterTask task) {
        ClusterTaskType clusterTaskType = task.type();
        switch (clusterTaskType) {
            case APPLY_MANIFEST -> {
                return handleApplyManifest();
            }
            case CHECK_READINESS -> {
                return handleCheckReadiness(task);
            }
            default -> throw new NotImplementedClusterTaskTypeException(task.type());
        }
    }

    private ClusterTaskProcessingResult handleApplyManifest(ClusterTask task) {
        Cluster cluster = clusterService.getCluster(task.clusterId());
        psmdbClient.applyManifest(cluster.name());

        return ClusterTaskProcessingResult.success();
    }

    private ClusterTaskProcessingResult handleCheckReadiness(ClusterTask task) {
        Cluster cluster = clusterService.getCluster(task.clusterId());

        boolean isReady = psmdbClient.isReady(cluster.name());
        if (!isReady) {
            return ClusterTaskProcessingResult.scheduled(
                    task.getScheduledAtWithDelay(Duration.ofSeconds(30))
            );
        }

        return ClusterTaskProcessingResult.success();
    }
}
