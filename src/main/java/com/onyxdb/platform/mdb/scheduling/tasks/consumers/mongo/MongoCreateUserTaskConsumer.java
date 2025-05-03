package com.onyxdb.platform.mdb.scheduling.tasks.consumers.mongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.mdb.clients.onyxdbAgent.OnyxdbAgentClient;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.databases.DatabaseRepository;
import com.onyxdb.platform.mdb.scheduling.tasks.consumers.TaskConsumer;
import com.onyxdb.platform.mdb.scheduling.tasks.models.Task;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskResult;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskType;
import com.onyxdb.platform.mdb.scheduling.operations.models.payloads.MongoCreateUserPayload;

public class MongoCreateUserTaskConsumer extends TaskConsumer<MongoCreateUserPayload> {
    private final OnyxdbAgentClient onyxdbAgentClient;
    private final DatabaseRepository databaseRepository;

    public MongoCreateUserTaskConsumer(
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
    protected TaskResult internalProcess(Task task, MongoCreateUserPayload payload) {
//        var rq = new CreateMongoUserRequestDTO(
//                payload.username(),
//                payload.passwordSecretName(),
//                payload.passwordSecretNamespace(),
//                payload.permissions().stream().map(p -> new MongoPermissionDTO(
//                        databaseRepository.getDatabase(payload, p.databaseId()).name(),
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
