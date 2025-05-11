package com.onyxdb.platform.mdb.operations.producers.mongo;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.clusters.models.CreateMongoPermission;
import com.onyxdb.platform.mdb.operations.models.Operation;
import com.onyxdb.platform.mdb.operations.models.ProducedTask;
import com.onyxdb.platform.mdb.operations.models.TaskType;
import com.onyxdb.platform.mdb.operations.models.payload.ClusterPayload;
import com.onyxdb.platform.mdb.operations.models.payload.MongoCreateClusterPayload;
import com.onyxdb.platform.mdb.operations.models.payload.MongoCreateDatabasePayload;
import com.onyxdb.platform.mdb.operations.models.payload.MongoCreateUserPayload;
import com.onyxdb.platform.mdb.operations.producers.TaskProducer;

@Component
public class MongoCreateClusterTaskProducer extends TaskProducer<MongoCreateClusterPayload> {
    public MongoCreateClusterTaskProducer(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public MongoCreateClusterPayload parsePayload(String payload) {
        try {
            return objectMapper.readValue(payload, MongoCreateClusterPayload.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ProducedTask> produceTasks(Operation operation, MongoCreateClusterPayload payload) {
        UUID operationId = operation.id();

        var clusterPayload = new ClusterPayload(payload.clusterId());

        var applyPsmdbTask = ProducedTask.createWithPayload(
                TaskType.MONGO_APPLY_PSMDB,
                operationId,
                List.of(),
                clusterPayload
        );
        var checkPsmdbReadinessTask = ProducedTask.createWithPayload(
                TaskType.MONGO_CHECK_PSMDB_READINESS,
                operationId,
                List.of(applyPsmdbTask.id()),
                clusterPayload
        );
        var applyOnyxdbAgentTask = ProducedTask.createWithPayload(
                TaskType.MONGO_APPLY_ONYXDB_AGENT,
                operationId,
                List.of(checkPsmdbReadinessTask.id()),
                clusterPayload
        );
        var checkOnyxdbAgentReadinessTask = ProducedTask.createWithPayload(
                TaskType.MONGO_CHECK_ONYXDB_AGENT_READINESS,
                operationId,
                List.of(applyOnyxdbAgentTask.id()),
                clusterPayload
        );
        var applyOnyxdbAgentServiceTask = ProducedTask.createWithPayload(
                TaskType.MONGO_APPLY_ONYXDB_AGENT_SERVICE,
                operationId,
                List.of(checkOnyxdbAgentReadinessTask.id()),
                clusterPayload
        );
        var applyExporterServiceTask = ProducedTask.createWithPayload(
                TaskType.MONGO_APPLY_EXPORTER_SERVICE,
                operationId,
                List.of(applyOnyxdbAgentServiceTask.id()),
                clusterPayload
        );
        var applyExporterServiceScrapeTask = ProducedTask.createWithPayload(
                TaskType.MONGO_APPLY_EXPORTER_SERVICE_SCRAPE,
                operationId,
                List.of(applyExporterServiceTask.id()),
                clusterPayload
        );

        var createDatabasePayload = new MongoCreateDatabasePayload(
                payload.clusterId(),
                payload.databaseName()
        );
        var createMongoDatabaseTask = ProducedTask.createWithPayload(
                TaskType.MONGO_CREATE_DATABASE,
                operationId,
                List.of(applyExporterServiceScrapeTask.id()),
                createDatabasePayload
        );

        var createMongoUserPayload = new MongoCreateUserPayload(
                payload.clusterId(),
                payload.userName(),
                payload.passwordSecretName(),
                payload.passwordSecretNamespace(),
                List.of(new CreateMongoPermission(
                        payload.userName(),
                        payload.databaseName(),
                        payload.clusterId(),
                        payload.roles()
                ))
        );
        var createMongoUserTask = ProducedTask.createWithPayload(
                TaskType.MONGO_CREATE_USER,
                operationId,
                List.of(createMongoDatabaseTask.id()),
                createMongoUserPayload
        );
        var markClusterReadyTask = ProducedTask.createWithPayload(
                TaskType.MONGO_MARK_CLUSTER_READY,
                operationId,
                List.of(createMongoUserTask.id()),
                clusterPayload
        );
        var finalTask = ProducedTask.create(
                TaskType.FINAL_TASK,
                operationId,
                List.of(markClusterReadyTask.id())
        );

        return List.of(
                applyPsmdbTask,
                checkPsmdbReadinessTask,
                applyOnyxdbAgentTask,
                checkOnyxdbAgentReadinessTask,
                applyOnyxdbAgentServiceTask,
                applyExporterServiceTask,
                applyExporterServiceScrapeTask,
                createMongoDatabaseTask,
                createMongoUserTask,
                markClusterReadyTask,
                finalTask
        );
    }
}
