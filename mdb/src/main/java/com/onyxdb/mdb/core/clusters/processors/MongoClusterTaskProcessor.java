package com.onyxdb.mdb.core.clusters.processors;

import java.time.Duration;
import java.util.List;

import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;
import org.springframework.stereotype.Component;

import com.onyxdb.mdb.clients.k8s.psmdb.Psmdb;
import com.onyxdb.mdb.clients.k8s.psmdb.PsmdbExporterService;
import com.onyxdb.mdb.clients.k8s.psmdb.PsmdbExporterServiceFactory;
import com.onyxdb.mdb.clients.k8s.psmdb.PsmdbFactory;
import com.onyxdb.mdb.clients.k8s.psmdb.PsmdbSpec;
import com.onyxdb.mdb.clients.k8s.victoriaMetrics.VmEndpoint;
import com.onyxdb.mdb.clients.k8s.victoriaMetrics.VmRelabelConfig;
import com.onyxdb.mdb.clients.k8s.victoriaMetrics.VmServiceScrape;
import com.onyxdb.mdb.clients.k8s.victoriaMetrics.VmServiceScrapeFactory;
import com.onyxdb.mdb.clients.k8s.victoriaMetrics.VmServiceScrapeSpec;
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
    private static final String DEFAULT_NAMESPACE = "onyxdb";

    private final PsmdbFactory psmdbFactory;
    private final PsmdbExporterServiceFactory psmdbExporterServiceFactory;
    private final VmServiceScrapeFactory vmServiceScrapeFactory;
    private final ClusterService clusterService;

    public MongoClusterTaskProcessor(
            PsmdbFactory psmdbFactory,
            PsmdbExporterServiceFactory psmdbExporterServiceFactory,
            VmServiceScrapeFactory vmServiceScrapeFactory,
            ClusterService clusterService
    ) {
        this.psmdbFactory = psmdbFactory;
        this.psmdbExporterServiceFactory = psmdbExporterServiceFactory;
        this.vmServiceScrapeFactory = vmServiceScrapeFactory;
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
                return handleCreateCluster(task);
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
            case MONGODB_CHECK_EXPORTER_SERVICE_SCRAPE_READINESS -> {
                return handleCheckExporterServiceScrapeReadiness(task);
            }
            default -> throw new NotImplementedClusterTaskTypeException(task.type());
        }
    }

    private ClusterTaskProcessingResult handleCreateCluster(ClusterTask task) {
        Cluster cluster = clusterService.getCluster(task.clusterId());

        var psmdb = new Psmdb(
                DEFAULT_NAMESPACE,
                cluster.name(),
                PsmdbSpec.builder().build(cluster.name())
        );
        psmdbFactory.createResource(psmdb);

        return ClusterTaskProcessingResult.success();
    }

    private ClusterTaskProcessingResult handleCheckReadiness(ClusterTask task) {
        Cluster cluster = clusterService.getCluster(task.clusterId());

        boolean isReady = psmdbFactory.isResourceReady(DEFAULT_NAMESPACE, cluster.name());
        if (!isReady) {
            return ClusterTaskProcessingResult.scheduled(
                    task.getScheduledAtWithDelay(Duration.ofSeconds(30))
            );
        }

        return ClusterTaskProcessingResult.success();
    }

    private ClusterTaskProcessingResult handleCreateExporterService(ClusterTask task) {
        Cluster cluster = clusterService.getCluster(task.clusterId());

        var psmdbExporterService = PsmdbExporterService.builder()
                .build(DEFAULT_NAMESPACE, cluster.name());
        psmdbExporterServiceFactory.createResource(psmdbExporterService);

        return ClusterTaskProcessingResult.success();
    }

    private ClusterTaskProcessingResult handleCheckExporterServiceReadiness(ClusterTask task) {
        Cluster cluster = clusterService.getCluster(task.clusterId());

        boolean isReady = psmdbExporterServiceFactory.resourceExists(DEFAULT_NAMESPACE, cluster.name());
        if (!isReady) {
            return ClusterTaskProcessingResult.scheduled(
                    task.getScheduledAtWithDelay(Duration.ofSeconds(30))
            );
        }

        return ClusterTaskProcessingResult.success();
    }

    private ClusterTaskProcessingResult handleCreateExporterServiceScrape(ClusterTask task) {
        Cluster cluster = clusterService.getCluster(task.clusterId());

        // TODO use real project name
        var vmServiceScrapeSpec = VmServiceScrapeSpec.builder()
                .withSelector(
                        new LabelSelectorBuilder()
                                .withMatchLabels(PsmdbExporterServiceFactory.getLabels(cluster.name()))
                                .build()
                )
                .withEndpoints(List.of(
                        new VmEndpoint(
                                PsmdbExporterServiceFactory.PORT_NAME,
                                PsmdbExporterServiceFactory.PATH,
                                List.of(
                                        new VmRelabelConfig(
                                                "project",
                                                "some-project"
                                        ),
                                        new VmRelabelConfig(
                                                "cluster",
                                                cluster.name()
                                        )
                                )
                        )
                ))
                .build();

        var vmServiceScrape = new VmServiceScrape(
                DEFAULT_NAMESPACE,
                PsmdbExporterServiceFactory.getPreparedName(cluster.name()),
                vmServiceScrapeSpec
        );
        vmServiceScrapeFactory.createResource(vmServiceScrape);

        return ClusterTaskProcessingResult.success();
    }

    private ClusterTaskProcessingResult handleCheckExporterServiceScrapeReadiness(ClusterTask task) {
        Cluster cluster = clusterService.getCluster(task.clusterId());

        boolean isReady = psmdbExporterServiceFactory.resourceExists(DEFAULT_NAMESPACE, cluster.name());
        if (!isReady) {
            return ClusterTaskProcessingResult.scheduled(
                    task.getScheduledAtWithDelay(Duration.ofSeconds(30))
            );
        }

        return ClusterTaskProcessingResult.success();
    }
}
