package com.onyxdb.platform.mdb.processing.consumers.mongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.clients.onyxdbAgent.OnyxdbAgentClient;
import com.onyxdb.platform.mdb.clients.onyxdbAgent.models.CreateMongoDatabaseRequestDTO;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.operationsOLD.tasks.payloads.MongoCreateDatabasePayload;
import com.onyxdb.platform.mdb.processing.consumers.TaskProcessor;
import com.onyxdb.platform.mdb.processing.models.Task;
import com.onyxdb.platform.mdb.processing.models.TaskProcessingResult;
import com.onyxdb.platform.mdb.processing.models.TaskType;

@Component
public class MongoCreateDatabaseTaskProcessor extends TaskProcessor<MongoCreateDatabasePayload> {
    private final OnyxdbAgentClient onyxdbAgentClient;

    public MongoCreateDatabaseTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService, OnyxdbAgentClient onyxdbAgentClient
    ) {
        super(objectMapper, clusterService);
        this.onyxdbAgentClient = onyxdbAgentClient;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_CREATE_DATABASE;
    }

    @Override
    protected TaskProcessingResult internalProcess(Task task, MongoCreateDatabasePayload payload) {
        var rq = new CreateMongoDatabaseRequestDTO(
                payload.databaseName()
        );
        onyxdbAgentClient.createDatabase(rq);

        return TaskProcessingResult.success();
    }

    @Override
    protected MongoCreateDatabasePayload parsePayload(Task task) throws JsonProcessingException {
        try {
            return objectMapper.readValue(task.payload(), MongoCreateDatabasePayload.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
