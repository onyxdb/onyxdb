package com.onyxdb.mdb.taskProcessing.processors.mongo;

import java.time.Duration;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.mdb.clients.k8s.psmdb.PsmdbExporterServiceClient;
import com.onyxdb.mdb.clients.k8s.victoriaMetrics.VmServiceScrapeClient;
import com.onyxdb.mdb.core.clusters.ClusterService;
import com.onyxdb.mdb.core.clusters.models.Cluster;
import com.onyxdb.mdb.taskProcessing.models.Task;
import com.onyxdb.mdb.taskProcessing.models.TaskProcessingResult;
import com.onyxdb.mdb.taskProcessing.models.TaskType;
import com.onyxdb.mdb.taskProcessing.models.payloads.ClusterTaskPayload;
import com.onyxdb.mdb.taskProcessing.processors.ClusterTaskProcessor;

import static com.onyxdb.mdb.core.clusters.ClusterMapper.DEFAULT_NAMESPACE;

public class MongoCheckExporterServiceScrapeReadinessTaskProcessor extends ClusterTaskProcessor {
    private final VmServiceScrapeClient vmServiceScrapeClient;

    public MongoCheckExporterServiceScrapeReadinessTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            VmServiceScrapeClient vmServiceScrapeClient
    ) {
        super(objectMapper, clusterService);
        this.vmServiceScrapeClient = vmServiceScrapeClient;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGODB_CHECK_EXPORTER_SERVICE_SCRAPE_READINESS;
    }

    @Override
    public TaskProcessingResult internalProcess(Task task, ClusterTaskPayload payload) {
        Cluster cluster = clusterService.getCluster(payload.clusterId());

        boolean isReady = vmServiceScrapeClient.resourceExists(
                DEFAULT_NAMESPACE,
                PsmdbExporterServiceClient.getPreparedName(cluster.name())
        );

        if (!isReady) {
            return TaskProcessingResult.scheduled(
                    task.getScheduledAtWithDelay(Duration.ofSeconds(30))
            );
        }

        return TaskProcessingResult.success();
    }
}
