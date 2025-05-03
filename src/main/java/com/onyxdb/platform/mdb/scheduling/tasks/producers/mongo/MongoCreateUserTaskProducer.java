package com.onyxdb.platform.mdb.scheduling.tasks.producers.mongo;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.scheduling.operations.models.Operation;
import com.onyxdb.platform.mdb.scheduling.operations.models.payloads.MongoCreateUserPayload;
import com.onyxdb.platform.mdb.scheduling.tasks.models.ProducedTask;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskType;
import com.onyxdb.platform.mdb.scheduling.tasks.producers.TaskProducer;

@Component
public class MongoCreateUserTaskProducer extends TaskProducer<MongoCreateUserPayload> {
    public MongoCreateUserTaskProducer(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public MongoCreateUserPayload parsePayload(String payload) {
        try {
            return objectMapper.readValue(payload, MongoCreateUserPayload.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ProducedTask> produceTasks(Operation operation, MongoCreateUserPayload payload) {
        UUID operationId = operation.id();

        var createDatabaseTask = ProducedTask.createWithPayload(
                TaskType.MONGO_CREATE_USER,
                operationId,
                List.of(),
                payload
        );
        var finalTask = ProducedTask.create(
                TaskType.FINAL_TASK,
                operationId,
                List.of(createDatabaseTask.id())
        );

        return List.of(
                createDatabaseTask,
                finalTask
        );
    }
}
