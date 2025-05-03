package com.onyxdb.platform.mdb.scheduling.tasks.producers.mongo;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.scheduling.operations.models.Operation;
import com.onyxdb.platform.mdb.scheduling.operations.models.payloads.ClusterPayload;
import com.onyxdb.platform.mdb.scheduling.tasks.models.ProducedTask;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskType;
import com.onyxdb.platform.mdb.scheduling.tasks.producers.TaskProducer;

@Component
public class MongoDeleteClusterTaskGenerator extends TaskProducer<ClusterPayload> {
    public MongoDeleteClusterTaskGenerator(ObjectMapper objectMapper) {
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

        var deleteExporterServiceScrapeTask = ProducedTask.createWithPayload(
                TaskType.MONGO_DELETE_EXPORTER_SERVICE_SCRAPE,
                operationId,
                List.of(),
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
        var finalTask = ProducedTask.create(
                TaskType.FINAL_TASK,
                operationId,
                List.of(checkPsmdbIsDeletedTask.id())
        );

        return List.of(
                deleteExporterServiceScrapeTask,
                deleteExporterServiceTask,
                deleteOnyxdbAgentTask,
                checkOnyxdbAgentIsDeletedTask,
                deletePsmdbTask,
                checkPsmdbIsDeletedTask,
                finalTask
        );
    }
}
