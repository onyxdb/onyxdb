package com.onyxdb.mdb.taskProcessing.processors.mongo;

import java.time.Duration;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.mdb.clients.k8s.KubernetesAdapter;
import com.onyxdb.mdb.core.clusters.ClusterService;
import com.onyxdb.mdb.core.clusters.models.Cluster;
import com.onyxdb.mdb.taskProcessing.models.Task;
import com.onyxdb.mdb.taskProcessing.models.TaskProcessingResult;
import com.onyxdb.mdb.taskProcessing.models.TaskType;
import com.onyxdb.mdb.taskProcessing.models.payloads.ClusterTaskPayload;
import com.onyxdb.mdb.taskProcessing.processors.ClusterTaskProcessor;

import static com.onyxdb.mdb.core.clusters.ClusterMapper.DEFAULT_PROJECT;

public class MongoCheckOnyxdbAgentIsDeletedTaskProcessor extends ClusterTaskProcessor {
    private final KubernetesAdapter kubernetesAdapter;

    public MongoCheckOnyxdbAgentIsDeletedTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            KubernetesAdapter kubernetesAdapter
    ) {
        super(objectMapper, clusterService);
        this.kubernetesAdapter = kubernetesAdapter;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGODB_CHECK_ONYXDB_AGENT_IS_DELETED;
    }

    @Override
    protected TaskProcessingResult internalProcess(Task task, ClusterTaskPayload payload) {
        Cluster cluster = clusterService.getCluster(payload.clusterId());

        boolean exists = kubernetesAdapter.onyxdbAgentExists(
                DEFAULT_PROJECT,
                cluster.id(),
                cluster.name()
        );
        if (exists) {
            return TaskProcessingResult.scheduled(
                    task.getScheduledAtWithDelay(Duration.ofSeconds(30))
            );
        }

        return TaskProcessingResult.success();
    }
}
