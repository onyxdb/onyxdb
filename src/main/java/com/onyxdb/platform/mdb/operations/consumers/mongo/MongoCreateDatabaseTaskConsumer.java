package com.onyxdb.platform.mdb.operations.consumers.mongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.clients.onyxdbAgent.OnyxdbAgentClient;
import com.onyxdb.platform.mdb.clients.onyxdbAgent.models.CreateMongoDatabaseRequestDTO;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.operations.consumers.TaskConsumer;
import com.onyxdb.platform.mdb.operations.models.Task;
import com.onyxdb.platform.mdb.operations.models.TaskResult;
import com.onyxdb.platform.mdb.operations.models.TaskType;
import com.onyxdb.platform.mdb.operations.models.payload.MongoCreateDatabasePayload;

@Component
public class MongoCreateDatabaseTaskConsumer extends TaskConsumer<MongoCreateDatabasePayload> {
    private final OnyxdbAgentClient onyxdbAgentClient;

    public MongoCreateDatabaseTaskConsumer(
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
    protected TaskResult internalProcess(Task task, MongoCreateDatabasePayload payload) {
        var rq = new CreateMongoDatabaseRequestDTO(payload.databaseName());
        onyxdbAgentClient.createDatabase(rq);

        return TaskResult.success();
    }

    @Override
    protected MongoCreateDatabasePayload parsePayload(Task task) throws JsonProcessingException {
        return objectMapper.readValue(task.payload(), MongoCreateDatabasePayload.class);
    }
}
