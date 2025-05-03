package com.onyxdb.platform.mdb.scheduling.tasks.producers.mongo;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.scheduling.operations.models.Operation;
import com.onyxdb.platform.mdb.scheduling.operations.models.payloads.MongoDeleteUserPayload;
import com.onyxdb.platform.mdb.scheduling.tasks.models.ProducedTask;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskType;
import com.onyxdb.platform.mdb.scheduling.tasks.producers.TaskProducer;

@Component
public class MongoDeleteUserTaskProducer extends TaskProducer<MongoDeleteUserPayload> {
    public MongoDeleteUserTaskProducer(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public MongoDeleteUserPayload parsePayload(String payload) {
        try {
            return objectMapper.readValue(payload, MongoDeleteUserPayload.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ProducedTask> produceTasks(Operation operation, MongoDeleteUserPayload payload) {
        UUID operationId = operation.id();

        var deleteDatabaseTask = ProducedTask.createWithPayload(
                TaskType.MONGO_DELETE_USER,
                operationId,
                List.of(),
                payload
        );
        var finalTask = ProducedTask.create(
                TaskType.FINAL_TASK,
                operationId,
                List.of(deleteDatabaseTask.id())
        );

        return List.of(
                deleteDatabaseTask,
                finalTask
        );
    }
}
