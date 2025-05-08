package com.onyxdb.platform.mdb.operations.producers.mongo;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.operations.models.Operation;
import com.onyxdb.platform.mdb.operations.models.ProducedTask;
import com.onyxdb.platform.mdb.operations.models.TaskType;
import com.onyxdb.platform.mdb.operations.models.payload.MongoDeleteClusterPayload;
import com.onyxdb.platform.mdb.operations.producers.TaskProducer;

@Component
public class MongoDeleteClusterTaskProducer extends TaskProducer<MongoDeleteClusterPayload> {
    public MongoDeleteClusterTaskProducer(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public List<ProducedTask> produceTasks(Operation operation, MongoDeleteClusterPayload payload) {
        UUID operationId = operation.id();

        var markClusterDeletingTask = ProducedTask.createWithPayload(
                TaskType.MONGO_MARK_CLUSTER_DELETING,
                operationId,
                List.of(),
                payload
        );
        var deleteExporterServiceScrapeTask = ProducedTask.createWithPayload(
                TaskType.MONGO_DELETE_EXPORTER_SERVICE_SCRAPE,
                operationId,
                List.of(markClusterDeletingTask.id()),
                payload
        );
        var deleteExporterServiceTask = ProducedTask.createWithPayload(
                TaskType.MONGO_DELETE_EXPORTER_SERVICE,
                operationId,
                List.of(deleteExporterServiceScrapeTask.id()),
                payload
        );
        var deleteOnyxdbAgentTask = ProducedTask.createWithPayload(
                TaskType.MONGO_DELETE_ONYXDB_AGENT,
                operationId,
                List.of(deleteExporterServiceTask.id()),
                payload
        );
        var checkOnyxdbAgentIsDeletedTask = ProducedTask.createWithPayload(
                TaskType.MONGO_CHECK_ONYXDB_AGENT_IS_DELETED,
                operationId,
                List.of(deleteOnyxdbAgentTask.id()),
                payload
        );
        var deletePsmdbTask = ProducedTask.createWithPayload(
                TaskType.MONGO_DELETE_PSMDB,
                operationId,
                List.of(checkOnyxdbAgentIsDeletedTask.id()),
                payload
        );
        var checkPsmdbIsDeletedTask = ProducedTask.createWithPayload(
                TaskType.MONGO_CHECK_PSMDB_IS_DELETED,
                operationId,
                List.of(deletePsmdbTask.id()),
                payload
        );
        var deleteSecretsTask = ProducedTask.createWithPayload(
                TaskType.MONGO_DELETE_SECRETS,
                operationId,
                List.of(checkPsmdbIsDeletedTask.id()),
                payload
        );
        var markClusterDeletedTask = ProducedTask.createWithPayload(
                TaskType.MONGO_MARK_CLUSTER_DELETED,
                operationId,
                List.of(deleteSecretsTask.id()),
                payload
        );
        var finalTask = ProducedTask.create(
                TaskType.FINAL_TASK,
                operationId,
                List.of(markClusterDeletedTask.id())
        );

        return List.of(
                markClusterDeletingTask,
                deleteExporterServiceScrapeTask,
                deleteExporterServiceTask,
                deleteOnyxdbAgentTask,
                checkOnyxdbAgentIsDeletedTask,
                deletePsmdbTask,
                checkPsmdbIsDeletedTask,
                deleteSecretsTask,
                markClusterDeletedTask,
                finalTask
        );
    }

    @Override
    public MongoDeleteClusterPayload parsePayload(String payload) throws JsonProcessingException {
        return objectMapper.readValue(payload, MongoDeleteClusterPayload.class);
    }
}
