package com.onyxdb.platform.mdb.scheduling.tasks.consumers.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbExporterServiceScrapeClient;
import com.onyxdb.platform.mdb.clients.k8s.victoriaMetrics.VmServiceScrapeClient;
import com.onyxdb.platform.mdb.clients.k8s.victoriaMetrics.adapters.MongoExporterServiceScrapeAdapter;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.scheduling.operations.models.payloads.ClusterPayload;
import com.onyxdb.platform.mdb.scheduling.tasks.consumers.ClusterTaskConsumer;
import com.onyxdb.platform.mdb.scheduling.tasks.models.Task;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskResult;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskType;

import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_NAMESPACE;
import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_PROJECT;

public class MongoDeleteExporterServiceScrapeTaskConsumer extends ClusterTaskConsumer {
    private final VmServiceScrapeClient vmServiceScrapeClient;
    private final MongoExporterServiceScrapeAdapter mongoExporterServiceScrapeAdapter;
    private final PsmdbExporterServiceScrapeClient psmdbExporterServiceScrapeClient;

    public MongoDeleteExporterServiceScrapeTaskConsumer(
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
    public TaskResult internalProcess(Task task, ClusterPayload payload) {
        Cluster cluster = clusterService.getClusterOrThrow(payload.clusterId());

        psmdbExporterServiceScrapeClient.deletePsmdbExporterServiceScrape(
                DEFAULT_NAMESPACE,
                DEFAULT_PROJECT,
                cluster.name()
        );

        return TaskResult.success();
    }
}
