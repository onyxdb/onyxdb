package com.onyxdb.platform.taskProcessing.processors.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.core.clusters.ClusterService;
import com.onyxdb.platform.core.clusters.models.Cluster;
import com.onyxdb.platform.taskProcessing.models.Task;
import com.onyxdb.platform.taskProcessing.models.TaskProcessingResult;
import com.onyxdb.platform.taskProcessing.models.TaskType;
import com.onyxdb.platform.taskProcessing.models.payloads.ClusterTaskPayload;
import com.onyxdb.platform.taskProcessing.processors.ClusterTaskProcessor;

import static com.onyxdb.platform.core.clusters.ClusterMapper.DEFAULT_NAMESPACE;
import static com.onyxdb.platform.core.clusters.ClusterMapper.DEFAULT_PROJECT;

public class MongoDeletePsmdbTaskProcessor extends ClusterTaskProcessor {
    private final PsmdbClient psmdbClient;

    public MongoDeletePsmdbTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient
    ) {
        super(objectMapper, clusterService);
        this.psmdbClient = psmdbClient;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGODB_DELETE_PSMDB;
    }

    @Override
    protected TaskProcessingResult internalProcess(Task task, ClusterTaskPayload payload) {
        Cluster cluster = clusterService.getCluster(payload.clusterId());
        psmdbClient.deletePsmdb(DEFAULT_NAMESPACE, DEFAULT_PROJECT, cluster.name());

        return TaskProcessingResult.success();
    }
}
