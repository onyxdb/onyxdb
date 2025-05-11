package com.onyxdb.platform.mdb.operations.consumers.mongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.clients.onyxdbAgent.OnyxdbAgentClient;
import com.onyxdb.platform.mdb.clients.onyxdbAgent.models.CreateMongoUserRequestDTO;
import com.onyxdb.platform.mdb.clients.onyxdbAgent.models.MongoPermissionDTO;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.clusters.models.MongoRole;
import com.onyxdb.platform.mdb.databases.DatabaseRepository;
import com.onyxdb.platform.mdb.operations.consumers.TaskConsumer;
import com.onyxdb.platform.mdb.operations.models.Task;
import com.onyxdb.platform.mdb.operations.models.TaskResult;
import com.onyxdb.platform.mdb.operations.models.TaskType;
import com.onyxdb.platform.mdb.operations.models.payload.MongoCreateUserPayload;
import com.onyxdb.platform.mdb.projects.Project;
import com.onyxdb.platform.mdb.projects.ProjectRepository;

@Component
public class MongoCreateUserTaskConsumer extends TaskConsumer<MongoCreateUserPayload> {
    private final OnyxdbAgentClient onyxdbAgentClient;
    private final DatabaseRepository databaseRepository;
    private final ProjectRepository projectRepository;

    public MongoCreateUserTaskConsumer(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            OnyxdbAgentClient onyxdbAgentClient,
            DatabaseRepository databaseRepository,
            ProjectRepository projectRepository
    ) {
        super(objectMapper, clusterService);
        this.onyxdbAgentClient = onyxdbAgentClient;
        this.databaseRepository = databaseRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_CREATE_USER;
    }

    @Override
    protected TaskResult internalProcess(Task task, MongoCreateUserPayload payload) {
        Cluster cluster = clusterService.getClusterOrThrow(payload.clusterId());
        Project project = projectRepository.getProjectOrThrow(cluster.projectId());

        var rq = new CreateMongoUserRequestDTO(
                payload.username(),
                payload.passwordSecretName(),
                payload.passwordSecretNamespace(),
                payload.permissions().stream().map(p -> new MongoPermissionDTO(
                        p.databaseName(),
                        p.roles().stream().map(MongoRole::value).toList()
                )).toList()
        );
        onyxdbAgentClient.createUser(
                cluster.namespace(),
                project.name(),
                cluster.name(),
                rq
        );

        return TaskResult.success();
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
