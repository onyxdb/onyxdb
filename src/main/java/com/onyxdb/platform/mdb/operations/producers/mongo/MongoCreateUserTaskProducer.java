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
import com.onyxdb.platform.mdb.operations.models.payload.MongoCreateUserPayload;
import com.onyxdb.platform.mdb.operations.producers.TaskProducer;

@Component
public class MongoCreateUserTaskProducer extends TaskProducer<MongoCreateUserPayload> {
    public MongoCreateUserTaskProducer(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public List<ProducedTask> produceTasks(Operation operation, MongoCreateUserPayload payload) {
        UUID operationId = operation.id();

        var clusterPayload = new ClusterPayload(payload.clusterId());

        var markClusterUpdatingTask = ProducedTask.createWithPayload(
                TaskType.MONGO_MARK_CLUSTER_UPDATING,
                operationId,
                List.of(),
                clusterPayload
        );
        var createUserTask = ProducedTask.createWithPayload(
                TaskType.MONGO_CREATE_USER,
                operationId,
                List.of(markClusterUpdatingTask.id()),
                payload
        );
        var markClusterReadyTask = ProducedTask.createWithPayload(
                TaskType.MONGO_MARK_CLUSTER_READY,
                operationId,
                List.of(createUserTask.id()),
                clusterPayload
        );
        var finalTask = ProducedTask.create(
                TaskType.FINAL_TASK,
                operationId,
                List.of(markClusterReadyTask.id())
        );

        return List.of(
                markClusterUpdatingTask,
                createUserTask,
                markClusterReadyTask,
                finalTask
        );
    }

    @Override
    public MongoCreateUserPayload parsePayload(String payload) throws JsonProcessingException {
        return objectMapper.readValue(payload, MongoCreateUserPayload.class);
    }
}
