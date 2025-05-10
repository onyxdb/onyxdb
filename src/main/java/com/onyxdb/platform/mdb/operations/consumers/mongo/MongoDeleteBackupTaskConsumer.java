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
import com.onyxdb.platform.mdb.operations.models.payload.MongoDeleteBackupPayload;

@Component
public class MongoDeleteBackupTaskConsumer extends TaskConsumer<MongoDeleteBackupPayload> {
    private final PsmdbClient psmdbClient;

    public MongoDeleteBackupTaskConsumer(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient
    ) {
        super(objectMapper, clusterService);
        this.psmdbClient = psmdbClient;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_DELETE_BACKUP;
    }

    @Override
    protected TaskResult internalProcess(Task task, MongoDeleteBackupPayload payload) {
        Cluster cluster = clusterService.getClusterOrThrow(payload.clusterId());
        psmdbClient.deletePsmdbBackup(cluster.namespace(), payload.backupName());

        return TaskResult.success();
    }

    @Override
    protected MongoDeleteBackupPayload parsePayload(Task task) throws JsonProcessingException {
        return objectMapper.readValue(task.payload(), MongoDeleteBackupPayload.class);
    }
}
