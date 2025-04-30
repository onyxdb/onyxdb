package com.onyxdb.platform.mdb.processing.producers.mongo;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.operationsOLD.tasks.ProducedTask;
import com.onyxdb.platform.mdb.processing.models.Operation;
import com.onyxdb.platform.mdb.processing.models.TaskType;
import com.onyxdb.platform.mdb.processing.models.payloads.MongoDeleteDatabasePayload;
import com.onyxdb.platform.mdb.processing.producers.TaskProducer;

@Component
public class MongoDeleteDatabaseTaskProducer extends TaskProducer<MongoDeleteDatabasePayload> {
    public MongoDeleteDatabaseTaskProducer(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public MongoDeleteDatabasePayload parsePayload(String payload) {
        try {
            return objectMapper.readValue(payload, MongoDeleteDatabasePayload.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ProducedTask> produceTasks(Operation operation, MongoDeleteDatabasePayload payload) {
        UUID operationId = operation.id();

        var deleteDatabaseTask = ProducedTask.createWithPayload(
                TaskType.MONGO_DELETE_DATABASE,
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
