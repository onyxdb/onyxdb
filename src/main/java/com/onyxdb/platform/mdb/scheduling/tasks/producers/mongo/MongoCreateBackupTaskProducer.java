package com.onyxdb.platform.mdb.scheduling.tasks.producers.mongo;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.scheduling.tasks.models.ProducedTask;
import com.onyxdb.platform.mdb.scheduling.operations.models.Operation;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskType;
import com.onyxdb.platform.mdb.scheduling.operations.models.payloads.ClusterPayload;
import com.onyxdb.platform.mdb.scheduling.tasks.producers.TaskProducer;

@Component
public class MongoCreateBackupTaskProducer extends TaskProducer<ClusterPayload> {
    public MongoCreateBackupTaskProducer(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public ClusterPayload parsePayload(String payload) {
        try {
            return objectMapper.readValue(payload, ClusterPayload.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ProducedTask> produceTasks(Operation operation, ClusterPayload payload) {
        UUID operationId = operation.id();

        var createBackupTask = ProducedTask.createWithPayload(
                TaskType.MONGO_CREATE_BACKUP,
                operationId,
                List.of(),
                payload
        );
        var finalTask = ProducedTask.create(
                TaskType.FINAL_TASK,
                operationId,
                List.of(createBackupTask.id())
        );

        return List.of(
                createBackupTask,
                finalTask
        );
    }
}
