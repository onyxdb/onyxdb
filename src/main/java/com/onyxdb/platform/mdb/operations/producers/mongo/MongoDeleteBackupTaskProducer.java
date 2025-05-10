package com.onyxdb.platform.mdb.operations.producers.mongo;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.operations.models.Operation;
import com.onyxdb.platform.mdb.operations.models.ProducedTask;
import com.onyxdb.platform.mdb.operations.models.TaskType;
import com.onyxdb.platform.mdb.operations.models.payload.ClusterPayload;
import com.onyxdb.platform.mdb.operations.models.payload.MongoDeleteBackupPayload;
import com.onyxdb.platform.mdb.operations.producers.TaskProducer;

@Component
public class MongoDeleteBackupTaskProducer extends TaskProducer<MongoDeleteBackupPayload> {
    public MongoDeleteBackupTaskProducer(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public List<ProducedTask> produceTasks(Operation operation, MongoDeleteBackupPayload payload) {
        UUID operationId = operation.id();

        var clusterPayload = new ClusterPayload(payload.clusterId());

        var markClusterUpdatingTask = ProducedTask.createWithPayload(
                TaskType.MONGO_MARK_CLUSTER_UPDATING,
                operationId,
                List.of(),
                clusterPayload
        );
        var deletedBackupTask = ProducedTask.createWithPayload(
                TaskType.MONGO_DELETE_BACKUP,
                operationId,
                List.of(markClusterUpdatingTask.id()),
                payload
        );
        var checkBackupIsDeletedTask = ProducedTask.createWithPayload(
                TaskType.MONGO_CHECK_BACKUP_IS_DELETED,
                operationId,
                List.of(deletedBackupTask.id()),
                payload
        );
        var markClusterReadyTask = ProducedTask.createWithPayload(
                TaskType.MONGO_MARK_CLUSTER_READY,
                operationId,
                List.of(checkBackupIsDeletedTask.id()),
                clusterPayload
        );
        var finalTask = ProducedTask.create(
                TaskType.FINAL_TASK,
                operationId,
                List.of(markClusterReadyTask.id())
        );

        return List.of(
                markClusterUpdatingTask,
                deletedBackupTask,
                checkBackupIsDeletedTask,
                markClusterReadyTask,
                finalTask
        );
    }

    @Override
    public MongoDeleteBackupPayload parsePayload(String payload) throws JsonProcessingException {
        return objectMapper.readValue(payload, MongoDeleteBackupPayload.class);
    }
}
