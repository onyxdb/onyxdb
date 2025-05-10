package com.onyxdb.platform.mdb.operations.consumers.mongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.operations.consumers.TaskConsumer;
import com.onyxdb.platform.mdb.operations.models.Task;
import com.onyxdb.platform.mdb.operations.models.TaskResult;
import com.onyxdb.platform.mdb.operations.models.TaskType;
import com.onyxdb.platform.mdb.operations.models.payload.MongoRestoreClusterPayload;

@Component
public class MongoCheckClusterIsRestoredTaskConsumer extends TaskConsumer<MongoRestoreClusterPayload> {
    private final PsmdbClient psmdbClient;

    public MongoCheckClusterIsRestoredTaskConsumer(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient
    ) {
        super(objectMapper, clusterService);
        this.psmdbClient = psmdbClient;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_CHECK_CLUSTER_IS_RESTORED;
    }

    @Override
    protected TaskResult internalProcess(Task task, MongoRestoreClusterPayload payload) {
        Cluster cluster = clusterService.getClusterOrThrow(payload.clusterId());

        boolean isRestored = psmdbClient.isPsmdbRestoreReady(cluster.namespace(), payload.backupName());
        if (!isRestored) {
            return TaskResult.error();
        }

        return TaskResult.success();
    }

    @Override
    protected MongoRestoreClusterPayload parsePayload(Task task) throws JsonProcessingException {
        return objectMapper.readValue(task.payload(), MongoRestoreClusterPayload.class);
    }
}
