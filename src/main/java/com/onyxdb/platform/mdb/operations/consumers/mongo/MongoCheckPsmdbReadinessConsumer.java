package com.onyxdb.platform.mdb.operations.consumers.mongo;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.clusters.models.Host;
import com.onyxdb.platform.mdb.hosts.HostService;
import com.onyxdb.platform.mdb.operations.consumers.ClusterTaskConsumer;
import com.onyxdb.platform.mdb.operations.models.Task;
import com.onyxdb.platform.mdb.operations.models.TaskResult;
import com.onyxdb.platform.mdb.operations.models.TaskType;
import com.onyxdb.platform.mdb.operations.models.payload.ClusterPayload;
import com.onyxdb.platform.mdb.projects.Project;
import com.onyxdb.platform.mdb.projects.ProjectRepository;

@Component
public class MongoCheckPsmdbReadinessConsumer extends ClusterTaskConsumer {
    private final PsmdbClient psmdbClient;
    private final HostService hostService;
    private final ProjectRepository projectRepository;

    public MongoCheckPsmdbReadinessConsumer(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient,
            HostService hostService,
            ProjectRepository projectRepository
    ) {
        super(objectMapper, clusterService);
        this.psmdbClient = psmdbClient;
        this.hostService = hostService;
        this.projectRepository = projectRepository;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_CHECK_PSMDB_READINESS;
    }

    @Override
    protected TaskResult internalProcess(Task task, ClusterPayload payload) {
        Cluster cluster = clusterService.getClusterOrThrow(payload.clusterId());
        Project project = projectRepository.getProjectOrThrow(cluster.projectId());

        boolean isReady = psmdbClient.isResourceReady(cluster.namespace(), project.name(), cluster.name());
        if (!isReady) {
            return TaskResult.error();
        }

        List<Host> hosts = psmdbClient.getPsmdbPods(cluster.namespace(), project.name(), cluster.name())
                .stream()
                .map(podName -> new Host(podName, cluster.id()))
                .toList();

        hostService.upsertHosts(hosts);

        return TaskResult.success();
    }
}
