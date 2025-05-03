package com.onyxdb.platform.mdb.scheduling.tasks.consumers.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.mdb.clients.k8s.KubernetesAdapter;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.scheduling.tasks.consumers.ClusterTaskConsumer;
import com.onyxdb.platform.mdb.scheduling.tasks.models.Task;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskResult;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskType;
import com.onyxdb.platform.mdb.scheduling.operations.models.payloads.ClusterPayload;

import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_PROJECT;

public class MongoApplyOnyxdbAgentTaskConsumer extends ClusterTaskConsumer {
    private final KubernetesAdapter kubernetesAdapter;

    public MongoApplyOnyxdbAgentTaskConsumer(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            KubernetesAdapter kubernetesAdapter
    ) {
        super(objectMapper, clusterService);
        this.kubernetesAdapter = kubernetesAdapter;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_APPLY_ONYXDB_AGENT;
    }

    @Override
    protected TaskResult internalProcess(Task task, ClusterPayload payload) {
        Cluster cluster = clusterService.getClusterOrThrow(payload.clusterId());

        kubernetesAdapter.applyOnyxdbAgent(
                DEFAULT_PROJECT,
                cluster.id(),
                cluster.name()
        );

        return TaskResult.success();
    }
}
