package com.onyxdb.platform.mdb.operations.consumers.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbExporterServiceScrapeClient;
import com.onyxdb.platform.mdb.clients.k8s.victoriaMetrics.VmServiceScrapeClient;
import com.onyxdb.platform.mdb.clients.k8s.victoriaMetrics.adapters.MongoExporterServiceScrapeAdapter;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.operations.consumers.ClusterTaskConsumer;
import com.onyxdb.platform.mdb.operations.models.Task;
import com.onyxdb.platform.mdb.operations.models.TaskResult;
import com.onyxdb.platform.mdb.operations.models.TaskType;
import com.onyxdb.platform.mdb.operations.models.payload.ClusterPayload;
import com.onyxdb.platform.mdb.projects.Project;
import com.onyxdb.platform.mdb.projects.ProjectRepository;

@Component
public class MongoCreateExporterServiceScrapeTaskConsumer extends ClusterTaskConsumer {
    private final VmServiceScrapeClient vmServiceScrapeClient;
    private final MongoExporterServiceScrapeAdapter mongoExporterServiceScrapeAdapter;
    private final PsmdbExporterServiceScrapeClient psmdbExporterServiceScrapeClient;
    private final ProjectRepository projectRepository;

    public MongoCreateExporterServiceScrapeTaskConsumer(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            VmServiceScrapeClient vmServiceScrapeClient,
            MongoExporterServiceScrapeAdapter mongoExporterServiceScrapeAdapter,
            PsmdbExporterServiceScrapeClient psmdbExporterServiceScrapeClient,
            ProjectRepository projectRepository
    ) {
        super(objectMapper, clusterService);
        this.vmServiceScrapeClient = vmServiceScrapeClient;
        this.mongoExporterServiceScrapeAdapter = mongoExporterServiceScrapeAdapter;
        this.psmdbExporterServiceScrapeClient = psmdbExporterServiceScrapeClient;
        this.projectRepository = projectRepository;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_APPLY_EXPORTER_SERVICE_SCRAPE;
    }

    @Override
    public TaskResult internalProcess(Task task, ClusterPayload payload) {
        Cluster cluster = clusterService.getClusterOrThrow(payload.clusterId());
        Project project = projectRepository.getProjectOrThrow(cluster.projectId());

        psmdbExporterServiceScrapeClient.applyPsmdbExporterServiceScrape(
                cluster.namespace(),
                project.name(),
                cluster.name()
        );

        return TaskResult.success();
    }
}
