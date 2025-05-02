package com.onyxdb.platform.mdb.processing.consumers.mongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.mdb.clients.onyxdbAgent.OnyxdbAgentClient;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.databases.DatabaseRepository;
import com.onyxdb.platform.mdb.processing.consumers.TaskProcessor;
import com.onyxdb.platform.mdb.processing.models.Task;
import com.onyxdb.platform.mdb.processing.models.TaskProcessingResult;
import com.onyxdb.platform.mdb.processing.models.TaskType;
import com.onyxdb.platform.mdb.processing.models.payloads.MongoCreateUserPayload;

public class MongoCreateUserTaskProcessor extends TaskProcessor<MongoCreateUserPayload> {
    private final OnyxdbAgentClient onyxdbAgentClient;
    private final DatabaseRepository databaseRepository;

    public MongoCreateUserTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            OnyxdbAgentClient onyxdbAgentClient,
            DatabaseRepository databaseRepository
    ) {
        super(objectMapper, clusterService);
        this.onyxdbAgentClient = onyxdbAgentClient;
        this.databaseRepository = databaseRepository;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_CREATE_USER;
    }

    @Override
    protected TaskProcessingResult internalProcess(Task task, MongoCreateUserPayload payload) {
//        var rq = new CreateMongoUserRequestDTO(
//                payload.username(),
//                payload.passwordSecretName(),
//                payload.passwordSecretNamespace(),
//                payload.permissions().stream().map(p -> new MongoPermissionDTO(
//                        databaseRepository.getDatabase(payload.id(), p.databaseId()).name(),
//                        p.roles().stream().map(MongoRole::value).toList()
//                )).toList()
//        );
//        onyxdbAgentClient.createUser(rq);
        throw new RuntimeException("FIXME");

//        return TaskProcessingResult.success();
    }

    @Override
    protected MongoCreateUserPayload parsePayload(Task task) throws JsonProcessingException {
        try {
            return objectMapper.readValue(task.payload(), MongoCreateUserPayload.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
