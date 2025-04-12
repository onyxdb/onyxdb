package com.onyxdb.mdb.taskProcessing.processors.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.mdb.clients.k8s.victoriaMetrics.VmServiceScrape;
import com.onyxdb.mdb.clients.k8s.victoriaMetrics.VmServiceScrapeClient;
import com.onyxdb.mdb.clients.k8s.victoriaMetrics.adapters.MongoExporterServiceScrapeAdapter;
import com.onyxdb.mdb.core.clusters.ClusterService;
import com.onyxdb.mdb.core.clusters.models.Cluster;
import com.onyxdb.mdb.taskProcessing.models.Task;
import com.onyxdb.mdb.taskProcessing.models.TaskProcessingResult;
import com.onyxdb.mdb.taskProcessing.models.TaskType;
import com.onyxdb.mdb.taskProcessing.models.payloads.ClusterTaskPayload;
import com.onyxdb.mdb.taskProcessing.processors.ClusterTaskProcessor;

import static com.onyxdb.mdb.core.clusters.ClusterMapper.DEFAULT_NAMESPACE;
import static com.onyxdb.mdb.core.clusters.ClusterMapper.DEFAULT_PROJECT;

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
