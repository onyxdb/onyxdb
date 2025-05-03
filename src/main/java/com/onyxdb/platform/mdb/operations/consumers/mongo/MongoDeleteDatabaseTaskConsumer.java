package com.onyxdb.platform.mdb.operations.consumers.mongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.clients.onyxdbAgent.OnyxdbAgentClient;
import com.onyxdb.platform.mdb.clients.onyxdbAgent.models.DeleteMongoDatabaseRequestDTO;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.databases.DatabaseRepository;
import com.onyxdb.platform.mdb.operations.consumers.TaskConsumer;
import com.onyxdb.platform.mdb.operations.models.Task;
import com.onyxdb.platform.mdb.operations.models.TaskResult;
import com.onyxdb.platform.mdb.operations.models.TaskType;
import com.onyxdb.platform.mdb.operations.models.payload.MongoDeleteDatabasePayload;
import com.onyxdb.platform.mdb.utils.OnyxdbConsts;

@Component
public class MongoDeleteDatabaseTaskConsumer extends TaskConsumer<MongoDeleteDatabasePayload> {
    private final OnyxdbAgentClient onyxdbAgentClient;
    private final DatabaseRepository databaseRepository;

    public MongoDeleteDatabaseTaskConsumer(
            ObjectMapper objectMapper,
            ClusterService clusterService, OnyxdbAgentClient onyxdbAgentClient, DatabaseRepository databaseRepository
    ) {
        super(objectMapper, clusterService);
        this.onyxdbAgentClient = onyxdbAgentClient;
        this.databaseRepository = databaseRepository;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_DELETE_DATABASE;
    }

    @Override
    protected MongoDeleteDatabasePayload parsePayload(Task task) throws JsonProcessingException {
        try {
            return objectMapper.readValue(task.payload(), MongoDeleteDatabasePayload.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected TaskResult internalProcess(Task task, MongoDeleteDatabasePayload payload) {
        onyxdbAgentClient.deleteDatabase(new DeleteMongoDatabaseRequestDTO(
                payload.databaseName()
        ));
        databaseRepository.markDatabaseAsDeleted(payload.databaseId(), OnyxdbConsts.USER_ID);

        return TaskResult.success();
    }
}
