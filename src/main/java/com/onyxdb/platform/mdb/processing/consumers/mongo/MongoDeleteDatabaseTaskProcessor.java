package com.onyxdb.platform.mdb.processing.consumers.mongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.clients.onyxdbAgent.OnyxdbAgentClient;
import com.onyxdb.platform.mdb.clients.onyxdbAgent.models.DeleteMongoDatabaseRequestDTO;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.databases.DatabaseRepository;
import com.onyxdb.platform.mdb.processing.consumers.TaskProcessor;
import com.onyxdb.platform.mdb.processing.models.Task;
import com.onyxdb.platform.mdb.processing.models.TaskProcessingResult;
import com.onyxdb.platform.mdb.processing.models.TaskType;
import com.onyxdb.platform.mdb.processing.models.payloads.MongoDeleteDatabasePayload;
import com.onyxdb.platform.mdb.utils.Consts;

@Component
public class MongoDeleteDatabaseTaskProcessor extends TaskProcessor<MongoDeleteDatabasePayload> {
    private final OnyxdbAgentClient onyxdbAgentClient;
    private final DatabaseRepository databaseRepository;

    public MongoDeleteDatabaseTaskProcessor(
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
    protected TaskProcessingResult internalProcess(Task task, MongoDeleteDatabasePayload payload) {
        onyxdbAgentClient.deleteDatabase(new DeleteMongoDatabaseRequestDTO(
                payload.databaseName()
        ));
        databaseRepository.markDatabaseAsDeleted(payload.databaseId(), Consts.USER_ID);

        return TaskProcessingResult.success();
    }
}
