package com.onyxdb.platform.mdb.operations.consumers.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.mdb.clusters.ClusterRepository;
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
public class MongoCheckPsmdbIsDeletedConsumer extends ClusterTaskConsumer {
    private final PsmdbClient psmdbClient;
    private final ClusterRepository clusterRepository;
    private final ProjectRepository projectRepository;

    public MongoCheckPsmdbIsDeletedConsumer(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient,
            ClusterRepository clusterRepository,
            ProjectRepository projectRepository
    ) {
        super(objectMapper, clusterService);
        this.psmdbClient = psmdbClient;
        this.clusterRepository = clusterRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_CHECK_PSMDB_IS_DELETED;
    }

    @Override
    protected TaskResult internalProcess(Task task, ClusterPayload payload) {
        Cluster cluster = clusterService.getClusterOrThrow(payload.clusterId());
        Project project = projectRepository.getProjectOrThrow(cluster.projectId());

        boolean psmdbExists = psmdbClient.psmdbExists(cluster.namespace(), project.name(), cluster.name());
        if (psmdbExists) {
            return TaskResult.error();
        }
        clusterRepository.markClusterDeleted(cluster.id());

        return TaskResult.success();
    }
}
