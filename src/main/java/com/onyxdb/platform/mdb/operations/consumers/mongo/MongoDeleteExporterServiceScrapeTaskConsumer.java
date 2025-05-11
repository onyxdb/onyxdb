package com.onyxdb.platform.mdb.operations.consumers.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbExporterServiceScrapeClient;
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
public class MongoDeleteExporterServiceScrapeTaskConsumer extends ClusterTaskConsumer {
    private final PsmdbExporterServiceScrapeClient psmdbExporterServiceScrapeClient;
    private final ProjectRepository projectRepository;

    public MongoDeleteExporterServiceScrapeTaskConsumer(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbExporterServiceScrapeClient psmdbExporterServiceScrapeClient,
            ProjectRepository projectRepository
    ) {
        super(objectMapper, clusterService);
        this.psmdbExporterServiceScrapeClient = psmdbExporterServiceScrapeClient;
        this.projectRepository = projectRepository;
    }

    public TaskType getTaskType() {
        return TaskType.MONGO_DELETE_EXPORTER_SERVICE_SCRAPE;
    }

    @Override
    public TaskResult internalProcess(Task task, ClusterPayload payload) {
        Cluster cluster = clusterService.getClusterOrThrow(payload.clusterId());
        Project project = projectRepository.getProjectOrThrow(cluster.projectId());

        psmdbExporterServiceScrapeClient.deletePsmdbExporterServiceScrape(
                cluster.namespace(),
                project.name(),
                cluster.name()
        );

        return TaskResult.success();
    }
}
