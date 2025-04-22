package com.onyxdb.platform.taskProcessing.processors.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.clients.k8s.victoriaMetrics.VmServiceScrape;
import com.onyxdb.platform.clients.k8s.victoriaMetrics.VmServiceScrapeClient;
import com.onyxdb.platform.clients.k8s.victoriaMetrics.adapters.MongoExporterServiceScrapeAdapter;
import com.onyxdb.platform.core.clusters.ClusterService;
import com.onyxdb.platform.core.clusters.models.Cluster;
import com.onyxdb.platform.taskProcessing.models.Task;
import com.onyxdb.platform.taskProcessing.models.TaskProcessingResult;
import com.onyxdb.platform.taskProcessing.models.TaskType;
import com.onyxdb.platform.taskProcessing.models.payloads.ClusterTaskPayload;
import com.onyxdb.platform.taskProcessing.processors.ClusterTaskProcessor;

import static com.onyxdb.platform.core.clusters.ClusterMapper.DEFAULT_NAMESPACE;
import static com.onyxdb.platform.core.clusters.ClusterMapper.DEFAULT_PROJECT;

public class MongoDeleteExporterServiceScrapeTaskProcessor extends ClusterTaskProcessor {
    private final VmServiceScrapeClient vmServiceScrapeClient;
    private final MongoExporterServiceScrapeAdapter mongoExporterServiceScrapeAdapter;

    public MongoDeleteExporterServiceScrapeTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            VmServiceScrapeClient vmServiceScrapeClient,
            MongoExporterServiceScrapeAdapter mongoExporterServiceScrapeAdapter
    ) {
        super(objectMapper, clusterService);
        this.vmServiceScrapeClient = vmServiceScrapeClient;
        this.mongoExporterServiceScrapeAdapter = mongoExporterServiceScrapeAdapter;
    }

    public TaskType getTaskType() {
        return TaskType.MONGODB_DELETE_EXPORTER_SERVICE_SCRAPE;
    }

    @Override
    public TaskProcessingResult internalProcess(Task task, ClusterTaskPayload payload) {
        Cluster cluster = clusterService.getCluster(payload.clusterId());

        VmServiceScrape vmServiceScrape = mongoExporterServiceScrapeAdapter.buildVmServiceScrape(
                DEFAULT_NAMESPACE,
                DEFAULT_PROJECT,
                cluster.name()
        );
        vmServiceScrapeClient.deleteResource(vmServiceScrape);

        return TaskProcessingResult.success();
    }
}
