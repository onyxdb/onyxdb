package com.onyxdb.platform.mdb.operations.consumers.mongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.mdb.clients.onyxdbAgent.OnyxdbAgentClient;
import com.onyxdb.platform.mdb.clients.onyxdbAgent.models.DeleteMongoUserRequestDTO;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.clusters.models.MongoPermission;
import com.onyxdb.platform.mdb.clusters.models.User;
import com.onyxdb.platform.mdb.operations.consumers.TaskConsumer;
import com.onyxdb.platform.mdb.operations.models.Task;
import com.onyxdb.platform.mdb.operations.models.TaskResult;
import com.onyxdb.platform.mdb.operations.models.TaskType;
import com.onyxdb.platform.mdb.operations.models.payload.MongoDeleteUserPayload;
import com.onyxdb.platform.mdb.projects.Project;
import com.onyxdb.platform.mdb.projects.ProjectRepository;
import com.onyxdb.platform.mdb.users.UserRepository;

@Component
public class MongoDeleteUserTaskConsumer extends TaskConsumer<MongoDeleteUserPayload> {
    private final OnyxdbAgentClient onyxdbAgentClient;
    private final UserRepository userRepository;
    private final TransactionTemplate transactionTemplate;
    private final ProjectRepository projectRepository;

    public MongoDeleteUserTaskConsumer(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            OnyxdbAgentClient onyxdbAgentClient,
            UserRepository userRepository,
            TransactionTemplate transactionTemplate,
            ProjectRepository projectRepository
    ) {
        super(objectMapper, clusterService);
        this.onyxdbAgentClient = onyxdbAgentClient;
        this.userRepository = userRepository;
        this.transactionTemplate = transactionTemplate;
        this.projectRepository = projectRepository;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_DELETE_USER;
    }

    @Override
    protected TaskResult internalProcess(Task task, MongoDeleteUserPayload payload) {
        Cluster cluster = clusterService.getClusterOrThrow(payload.clusterId());
        Project project = projectRepository.getProjectOrThrow(cluster.projectId());

        User user = userRepository.getUser(payload.clusterId(), payload.userName());

        onyxdbAgentClient.deleteUser(
                cluster.namespace(),
                project.name(),
                cluster.name(),
                new DeleteMongoUserRequestDTO(payload.userName())
        );

        transactionTemplate.executeWithoutResult(status -> {
            userRepository.markUserAsDeleted(user.id(), payload.deletedBy());
            userRepository.markPermissionsAsDeleted(
                    user.permissions().stream().map(MongoPermission::id).toList(),
                    payload.deletedBy()
            );
        });

        return TaskResult.success();
    }

    @Override
    protected MongoDeleteUserPayload parsePayload(Task task) throws JsonProcessingException {
        return objectMapper.readValue(task.payload(), MongoDeleteUserPayload.class);
    }
}
