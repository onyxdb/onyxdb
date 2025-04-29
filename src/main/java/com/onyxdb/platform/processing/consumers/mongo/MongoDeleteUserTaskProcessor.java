package com.onyxdb.platform.processing.consumers.mongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.clients.onyxdbAgent.OnyxdbAgentClient;
import com.onyxdb.platform.clients.onyxdbAgent.models.DeleteMongoDatabaseRequestDTO;
import com.onyxdb.platform.clients.onyxdbAgent.models.DeleteMongoUserRequestDTO;
import com.onyxdb.platform.core.clusters.models.User;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.databases.DatabaseRepository;
import com.onyxdb.platform.mdb.users.UserRepository;
import com.onyxdb.platform.processing.consumers.TaskProcessor;
import com.onyxdb.platform.processing.models.Task;
import com.onyxdb.platform.processing.models.TaskProcessingResult;
import com.onyxdb.platform.processing.models.TaskType;
import com.onyxdb.platform.processing.models.payloads.MongoDeleteDatabasePayload;
import com.onyxdb.platform.processing.models.payloads.MongoDeleteUserPayload;
import com.onyxdb.platform.utils.Consts;

@Component
public class MongoDeleteUserTaskProcessor extends TaskProcessor<MongoDeleteUserPayload> {
    private final OnyxdbAgentClient onyxdbAgentClient;
    private final UserRepository userRepository;

    public MongoDeleteUserTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            OnyxdbAgentClient onyxdbAgentClient,
            UserRepository userRepository
    ) {
        super(objectMapper, clusterService);
        this.onyxdbAgentClient = onyxdbAgentClient;
        this.userRepository = userRepository;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_DELETE_DATABASE;
    }

    @Override
    protected MongoDeleteUserPayload parsePayload(Task task) throws JsonProcessingException {
        try {
            return objectMapper.readValue(task.payload(), MongoDeleteUserPayload.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected TaskProcessingResult internalProcess(Task task, MongoDeleteUserPayload payload) {
        User user = userRepository.getUser(payload.userId());

        onyxdbAgentClient.deleteUser(new DeleteMongoUserRequestDTO(payload.username()));
        userRepository.markUserAsDeleted(user.id(), Consts.USER_ID);

        return TaskProcessingResult.success();
    }
}
