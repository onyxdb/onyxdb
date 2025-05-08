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
import com.onyxdb.platform.mdb.operations.models.payload.MongoDeleteDatabasePayload;
import com.onyxdb.platform.mdb.operations.producers.TaskProducer;

@Component
public class MongoDeleteDatabaseTaskProducer extends TaskProducer<MongoDeleteDatabasePayload> {
    public MongoDeleteDatabaseTaskProducer(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public List<ProducedTask> produceTasks(Operation operation, MongoDeleteDatabasePayload payload) {
        UUID operationId = operation.id();

        var clusterPayload = new ClusterPayload(payload.clusterId());

        var markClusterUpdatingTask = ProducedTask.createWithPayload(
                TaskType.MONGO_MARK_CLUSTER_UPDATING,
                operationId,
                List.of(),
                clusterPayload
        );
        var deleteDatabaseTask = ProducedTask.createWithPayload(
                TaskType.MONGO_DELETE_DATABASE,
                operationId,
                List.of(markClusterUpdatingTask.id()),
                payload
        );
        var markClusterReadyTask = ProducedTask.createWithPayload(
                TaskType.MONGO_MARK_CLUSTER_READY,
                operationId,
                List.of(deleteDatabaseTask.id()),
                clusterPayload
        );
        var finalTask = ProducedTask.create(
                TaskType.FINAL_TASK,
                operationId,
                List.of(markClusterReadyTask.id())
        );

        return List.of(
                markClusterUpdatingTask,
                deleteDatabaseTask,
                markClusterReadyTask,
                finalTask
        );
    }

    @Override
    public MongoDeleteDatabasePayload parsePayload(String payload) throws JsonProcessingException {
        return objectMapper.readValue(payload, MongoDeleteDatabasePayload.class);
    }
}
