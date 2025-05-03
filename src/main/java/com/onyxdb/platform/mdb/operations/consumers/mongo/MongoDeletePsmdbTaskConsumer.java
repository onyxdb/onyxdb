package com.onyxdb.platform.mdb.scheduling.tasks.consumers.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.scheduling.operations.models.payloads.ClusterPayload;
import com.onyxdb.platform.mdb.scheduling.tasks.consumers.ClusterTaskConsumer;
import com.onyxdb.platform.mdb.scheduling.tasks.models.Task;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskResult;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskType;

import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_NAMESPACE;
import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_PROJECT;

public class MongoDeletePsmdbTaskConsumer extends ClusterTaskConsumer {
    private final PsmdbClient psmdbClient;

    public MongoDeletePsmdbTaskConsumer(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient
    ) {
        super(objectMapper, clusterService);
        this.psmdbClient = psmdbClient;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_DELETE_PSMDB;
    }

    @Override
    protected TaskResult internalProcess(Task task, ClusterPayload payload) {
        Cluster cluster = clusterService.getClusterOrThrow(payload.clusterId());
        psmdbClient.deletePsmdb(DEFAULT_NAMESPACE, DEFAULT_PROJECT, cluster.name());

        return TaskResult.success();
    }
}
