package com.onyxdb.platform.mdb.operations.consumers.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.clients.k8s.KubernetesAdapter;
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
public class MongoCheckOnyxdbAgentReadinessTaskConsumer extends ClusterTaskConsumer {
    private final KubernetesAdapter kubernetesAdapter;
    private final ProjectRepository projectRepository;

    public MongoCheckOnyxdbAgentReadinessTaskConsumer(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            KubernetesAdapter kubernetesAdapter,
            ProjectRepository projectRepository
    ) {
        super(objectMapper, clusterService);
        this.kubernetesAdapter = kubernetesAdapter;
        this.projectRepository = projectRepository;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_CHECK_ONYXDB_AGENT_READINESS;
    }

    @Override
    protected TaskResult internalProcess(Task task, ClusterPayload payload) {
        Cluster cluster = clusterService.getClusterOrThrow(payload.clusterId());
        Project project = projectRepository.getProjectOrThrow(cluster.projectId());

        boolean isReady = kubernetesAdapter.isOnyxdbAgentReady(
                project.namespace(),
                project.name(),
                cluster.id(),
                cluster.name()
        );

        if (!isReady) {
            return TaskResult.error();
        }

        return TaskResult.success();
    }
}
