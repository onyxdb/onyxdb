package com.onyxdb.platform.taskProcessing.processors.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.clients.k8s.KubernetesAdapter;
import com.onyxdb.platform.core.clusters.ClusterService;
import com.onyxdb.platform.core.clusters.models.Cluster;
import com.onyxdb.platform.taskProcessing.models.Task;
import com.onyxdb.platform.taskProcessing.models.TaskProcessingResult;
import com.onyxdb.platform.taskProcessing.models.TaskType;
import com.onyxdb.platform.taskProcessing.models.payloads.ClusterTaskPayload;
import com.onyxdb.platform.taskProcessing.processors.ClusterTaskProcessor;

import static com.onyxdb.platform.core.clusters.ClusterMapper.DEFAULT_PROJECT;

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
        return TaskType.MONGODB_APPLY_ONYXDB_AGENT;
    }

    @Override
    protected TaskProcessingResult internalProcess(Task task, ClusterTaskPayload payload) {
        Cluster cluster = clusterService.getCluster(payload.clusterId());

        kubernetesAdapter.applyOnyxdbAgent(
                DEFAULT_PROJECT,
                cluster.id(),
                cluster.name()
        );

        return TaskProcessingResult.success();
    }
}
