package com.onyxdb.platform.mdb.scheduling.tasks.producers.mongo;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.scheduling.operations.models.Operation;
import com.onyxdb.platform.mdb.scheduling.operations.models.payloads.ClusterPayload;
import com.onyxdb.platform.mdb.scheduling.operations.models.payloads.MongoScaleClusterPayload;
import com.onyxdb.platform.mdb.scheduling.tasks.models.ProducedTask;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskType;
import com.onyxdb.platform.mdb.scheduling.tasks.producers.TaskProducer;

@Component
public class MongoScaleClusterTaskProducer extends TaskProducer<MongoScaleClusterPayload> {
    public MongoScaleClusterTaskProducer(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public MongoScaleClusterPayload parsePayload(String payload) {
        try {
            return objectMapper.readValue(payload, MongoScaleClusterPayload.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ProducedTask> produceTasks(Operation operation, MongoScaleClusterPayload payload) {
        UUID operationId = operation.id();

        var clusterPayload = new ClusterPayload(
                payload.clusterId()
        );
        var applyPsmdbTask = ProducedTask.createWithPayload(
                TaskType.MONGO_APPLY_PSMDB,
                operationId,
                List.of(),
                clusterPayload
        );
        var updateHostsTask = ProducedTask.createWithPayload(
                TaskType.MONGO_UPDATE_HOSTS,
                operationId,
                List.of(applyPsmdbTask.id()),
                clusterPayload
        );
        var finalTask = ProducedTask.create(
                TaskType.FINAL_TASK,
                operationId,
                List.of(updateHostsTask.id())
        );

        return List.of(
                applyPsmdbTask,
                updateHostsTask,
                finalTask
        );
    }
}
