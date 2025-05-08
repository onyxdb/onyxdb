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
import com.onyxdb.platform.mdb.operations.models.payload.MongoModifyClusterPayload;
import com.onyxdb.platform.mdb.operations.producers.TaskProducer;

@Component
public class MongoModifyClusterTaskProducer extends TaskProducer<MongoModifyClusterPayload> {
    public MongoModifyClusterTaskProducer(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public List<ProducedTask> produceTasks(Operation operation, MongoModifyClusterPayload payload) {
        UUID operationId = operation.id();

        var clusterPayload = new ClusterPayload(
                payload.clusterId()
        );

        var markClusterUpdatingTask = ProducedTask.createWithPayload(
                TaskType.MONGO_MARK_CLUSTER_UPDATING,
                operationId,
                List.of(),
                clusterPayload
        );
        var applyPsmdbTask = ProducedTask.createWithPayload(
                TaskType.MONGO_APPLY_PSMDB,
                operationId,
                List.of(markClusterUpdatingTask.id()),
                clusterPayload
        );
        var checkPsmdbReadinessTask = ProducedTask.createWithPayload(
                TaskType.MONGO_CHECK_PSMDB_READINESS,
                operationId,
                List.of(applyPsmdbTask.id()),
                clusterPayload
        );
        var updateHostsTask = ProducedTask.createWithPayload(
                TaskType.MONGO_UPDATE_HOSTS,
                operationId,
                List.of(checkPsmdbReadinessTask.id()),
                clusterPayload
        );
        var markClusterReadyTask = ProducedTask.createWithPayload(
                TaskType.MONGO_MARK_CLUSTER_READY,
                operationId,
                List.of(updateHostsTask.id()),
                clusterPayload
        );
        var finalTask = ProducedTask.create(
                TaskType.FINAL_TASK,
                operationId,
                List.of(markClusterReadyTask.id())
        );

        return List.of(
                markClusterUpdatingTask,
                applyPsmdbTask,
                checkPsmdbReadinessTask,
                updateHostsTask,
                markClusterReadyTask,
                finalTask
        );
    }

    @Override
    public MongoModifyClusterPayload parsePayload(String payload) {
        try {
            return objectMapper.readValue(payload, MongoModifyClusterPayload.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
