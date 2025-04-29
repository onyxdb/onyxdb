package com.onyxdb.platform.processing.consumers.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.clients.k8s.KubernetesAdapter;
import com.onyxdb.platform.core.clusters.models.Cluster;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.processing.consumers.ClusterTaskProcessor;
import com.onyxdb.platform.processing.models.Task;
import com.onyxdb.platform.processing.models.TaskProcessingResult;
import com.onyxdb.platform.processing.models.TaskType;
import com.onyxdb.platform.processing.models.payloads.ClusterPayload;

import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_PROJECT;

public class MongoApplyOnyxdbAgentTaskProcessor extends ClusterTaskProcessor {
    private final KubernetesAdapter kubernetesAdapter;

    public MongoApplyOnyxdbAgentTaskProcessor(
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
    protected TaskProcessingResult internalProcess(Task task, ClusterPayload payload) {
        Cluster cluster = clusterService.getCluster(payload.clusterId());

        kubernetesAdapter.applyOnyxdbAgent(
                DEFAULT_PROJECT,
                cluster.id(),
                cluster.name()
        );

        return TaskProcessingResult.success();
    }
}
