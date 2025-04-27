package com.onyxdb.platform.processing.consumers.mongo;

import java.time.Duration;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.clients.k8s.KubernetesAdapter;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.core.clusters.models.Cluster;
import com.onyxdb.platform.processing.models.Task;
import com.onyxdb.platform.processing.models.TaskProcessingResult;
import com.onyxdb.platform.processing.models.TaskType;
import com.onyxdb.platform.processing.models.payloads.ClusterPayload;
import com.onyxdb.platform.processing.consumers.ClusterTaskProcessor;

import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_PROJECT;

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
        return TaskType.MONGO_CHECK_ONYXDB_AGENT_IS_DELETED;
    }

    @Override
    protected TaskProcessingResult internalProcess(Task task, ClusterPayload payload) {
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
