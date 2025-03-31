package com.onyxdb.mdb.core.clusters.processors;

import java.time.Duration;
import java.util.List;

import org.springframework.stereotype.Component;

import com.onyxdb.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.mdb.clients.k8s.victoriametrics.ServiceScrapeEndpoint;
import com.onyxdb.mdb.clients.k8s.victoriametrics.VmOperatorClient;
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
    private final VmOperatorClient vmOperatorClient;
    private final ClusterService clusterService;

    public MongoClusterTaskProcessor(
            PsmdbClient psmdbClient,
            VmOperatorClient vmOperatorClient,
            ClusterService clusterService
    ) {
        this.psmdbClient = psmdbClient;
        this.vmOperatorClient = vmOperatorClient;
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
            case MONGODB_CREATE_CLUSTER -> {
                return handleApplyManifest(task);
            }
            case MONGODB_CHECK_CLUSTER_READINESS -> {
                return handleCheckReadiness(task);
            }
            case MONGODB_CREATE_EXPORTER_SERVICE -> {
                return handleCreateExporterService(task);
            }
            case MONGODB_CHECK_EXPORTER_SERVICE_READINESS -> {
                return handleCheckExporterServiceReadiness(task);
            }
            case MONGODB_CREATE_EXPORTER_SERVICE_SCRAPE -> {
                return handleCreateExporterServiceScrape(task);
            }
            case MONGODB_CREATE_GRAFANA_DASHBOARD -> {
                return handleCreateGrafanaDashboard(task);
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

    private ClusterTaskProcessingResult handleCreateExporterService(ClusterTask task) {
        Cluster cluster = clusterService.getCluster(task.clusterId());
        psmdbClient.createExporterService(cluster.name());

        return ClusterTaskProcessingResult.success();
    }

    private ClusterTaskProcessingResult handleCheckExporterServiceReadiness(ClusterTask task) {
        Cluster cluster = clusterService.getCluster(task.clusterId());

        boolean isReady = psmdbClient.isExporterServiceReady(cluster.name());
        if (!isReady) {
            return ClusterTaskProcessingResult.scheduled(
                    task.getScheduledAtWithDelay(Duration.ofSeconds(30))
            );
        }

        return ClusterTaskProcessingResult.success();
    }

    private ClusterTaskProcessingResult handleCreateExporterServiceScrape(ClusterTask task) {
        // TODO maybe we should check if vm synced config and found new cluster
        Cluster cluster = clusterService.getCluster(task.clusterId());

        vmOperatorClient.createServiceScrape(
                psmdbClient.getExporterServiceNameByClusterName(cluster.name()),
                psmdbClient.getExporterServiceLabelsByClusterName(cluster.name()),
                List.of(
                        new ServiceScrapeEndpoint(
                                psmdbClient.getExporterServicePortName(),
                                psmdbClient.getExporterServiceMetricsPath()
                        )
                )
        );

        return ClusterTaskProcessingResult.success();
    }

    private ClusterTaskProcessingResult handleCreateGrafanaDashboard(ClusterTask task) {
        return ClusterTaskProcessingResult.success();
    }
}
