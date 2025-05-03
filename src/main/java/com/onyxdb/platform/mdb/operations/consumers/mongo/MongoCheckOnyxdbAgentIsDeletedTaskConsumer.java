package com.onyxdb.platform.mdb.operations.consumers.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.mdb.clients.k8s.KubernetesAdapter;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.operations.consumers.ClusterTaskConsumer;
import com.onyxdb.platform.mdb.operations.models.Task;
import com.onyxdb.platform.mdb.operations.models.TaskResult;
import com.onyxdb.platform.mdb.operations.models.TaskType;
import com.onyxdb.platform.mdb.operations.models.payload.ClusterPayload;

import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_PROJECT;

public class MongoCheckOnyxdbAgentIsDeletedTaskConsumer extends ClusterTaskConsumer {
    private final KubernetesAdapter kubernetesAdapter;

    public MongoCheckOnyxdbAgentIsDeletedTaskConsumer(
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
    protected TaskResult internalProcess(Task task, ClusterPayload payload) {
        Cluster cluster = clusterService.getClusterOrThrow(payload.clusterId());

        boolean exists = kubernetesAdapter.onyxdbAgentExists(
                DEFAULT_PROJECT,
                cluster.id(),
                cluster.name()
        );
        if (exists) {
            return TaskResult.error();
//            return TaskResult.scheduled(
//                    task.getScheduledAtWithDelay(Duration.ofSeconds(30))
//            );
        }

        return TaskResult.success();
    }
}
