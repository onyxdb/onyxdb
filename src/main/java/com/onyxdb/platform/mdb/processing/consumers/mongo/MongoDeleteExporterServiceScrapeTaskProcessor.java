package com.onyxdb.platform.mdb.processing.consumers.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbExporterServiceScrapeClient;
import com.onyxdb.platform.mdb.clients.k8s.victoriaMetrics.VmServiceScrapeClient;
import com.onyxdb.platform.mdb.clients.k8s.victoriaMetrics.adapters.MongoExporterServiceScrapeAdapter;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.models.Cluster;
import com.onyxdb.platform.mdb.processing.consumers.ClusterTaskProcessor;
import com.onyxdb.platform.mdb.processing.models.Task;
import com.onyxdb.platform.mdb.processing.models.TaskProcessingResult;
import com.onyxdb.platform.mdb.processing.models.TaskType;
import com.onyxdb.platform.mdb.processing.models.payloads.ClusterPayload;

import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_NAMESPACE;
import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_PROJECT;

public class MongoDeleteExporterServiceScrapeTaskProcessor extends ClusterTaskProcessor {
    private final VmServiceScrapeClient vmServiceScrapeClient;
    private final MongoExporterServiceScrapeAdapter mongoExporterServiceScrapeAdapter;
    private final PsmdbExporterServiceScrapeClient psmdbExporterServiceScrapeClient;

    public MongoDeleteExporterServiceScrapeTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            VmServiceScrapeClient vmServiceScrapeClient,
            MongoExporterServiceScrapeAdapter mongoExporterServiceScrapeAdapter, PsmdbExporterServiceScrapeClient psmdbExporterServiceScrapeClient
    ) {
        super(objectMapper, clusterService);
        this.vmServiceScrapeClient = vmServiceScrapeClient;
        this.mongoExporterServiceScrapeAdapter = mongoExporterServiceScrapeAdapter;
        this.psmdbExporterServiceScrapeClient = psmdbExporterServiceScrapeClient;
    }

    public TaskType getTaskType() {
        return TaskType.MONGO_DELETE_EXPORTER_SERVICE_SCRAPE;
    }

    @Override
    public TaskProcessingResult internalProcess(Task task, ClusterPayload payload) {
        Cluster cluster = clusterService.getCluster(payload.clusterId());

        psmdbExporterServiceScrapeClient.deletePsmdbExporterServiceScrape(
                DEFAULT_NAMESPACE,
                DEFAULT_PROJECT,
                cluster.name()
        );

        return TaskProcessingResult.success();
    }
}
