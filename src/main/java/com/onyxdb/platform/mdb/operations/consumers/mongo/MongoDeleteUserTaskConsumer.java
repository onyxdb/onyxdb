package com.onyxdb.platform.mdb.scheduling.tasks.consumers.mongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.clients.onyxdbAgent.OnyxdbAgentClient;
import com.onyxdb.platform.mdb.clients.onyxdbAgent.models.DeleteMongoUserRequestDTO;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.clusters.models.User;
import com.onyxdb.platform.mdb.scheduling.operations.models.payloads.MongoDeleteUserPayload;
import com.onyxdb.platform.mdb.scheduling.tasks.consumers.TaskConsumer;
import com.onyxdb.platform.mdb.scheduling.tasks.models.Task;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskResult;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskType;
import com.onyxdb.platform.mdb.users.UserRepository;
import com.onyxdb.platform.mdb.utils.OnyxdbConsts;

@Component
public class MongoDeleteUserTaskConsumer extends TaskConsumer<MongoDeleteUserPayload> {
    private final OnyxdbAgentClient onyxdbAgentClient;
    private final UserRepository userRepository;

    public MongoDeleteUserTaskConsumer(
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
    protected TaskResult internalProcess(Task task, MongoDeleteUserPayload payload) {
        User user = userRepository.getUser(payload.userId());

        onyxdbAgentClient.deleteUser(new DeleteMongoUserRequestDTO(payload.username()));
        userRepository.markUserAsDeleted(user.id(), OnyxdbConsts.USER_ID);

        return TaskResult.success();
    }
}
