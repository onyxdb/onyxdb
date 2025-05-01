package com.onyxdb.platform.mdb.processing.producers.mongo;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.models.CreateMongoPermission;
import com.onyxdb.platform.mdb.operationsOLD.tasks.ProducedTask;
import com.onyxdb.platform.mdb.processing.models.Operation;
import com.onyxdb.platform.mdb.processing.models.TaskType;
import com.onyxdb.platform.mdb.processing.models.payloads.ClusterPayload;
import com.onyxdb.platform.mdb.processing.models.payloads.MongoCreateClusterPayload;
import com.onyxdb.platform.mdb.processing.models.payloads.MongoCreateDatabasePayload;
import com.onyxdb.platform.mdb.processing.models.payloads.MongoCreateUserPayload;
import com.onyxdb.platform.mdb.processing.producers.TaskProducer;

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

        var clusterPayload = new ClusterPayload(
                payload.clusterId()
        );
        var applyMongoVectorConfigTask = ProducedTask.createWithPayload(
                TaskType.MONGO_APPLY_VECTOR_CONFIG,
                operationId,
                List.of(),
                clusterPayload
        );
        var applyPsmdbTask = ProducedTask.createWithPayload(
                TaskType.MONGO_APPLY_PSMDB,
                operationId,
                List.of(applyMongoVectorConfigTask.id()),
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
        var applyExporterServiceTask = ProducedTask.createWithPayload(
                TaskType.MONGO_APPLY_EXPORTER_SERVICE,
                operationId,
                List.of(checkOnyxdbAgentReadinessTask.id()),
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
                        payload.databaseName(),
                        payload.userName(),
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
        var finalTask = ProducedTask.create(
                TaskType.FINAL_TASK,
                operationId,
                List.of(createMongoUserTask.id())
        );

        return List.of(
                applyMongoVectorConfigTask,
                applyPsmdbTask,
                checkPsmdbReadinessTask,
                applyOnyxdbAgentTask,
                checkOnyxdbAgentReadinessTask,
                applyExporterServiceTask,
                applyExporterServiceScrapeTask,
                createMongoDatabaseTask,
                createMongoUserTask,
                finalTask
        );
    }
}
