package com.onyxdb.platform.mdb.users;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.mdb.clusters.ClusterRepository;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.clusters.models.CreateUser;
import com.onyxdb.platform.mdb.clusters.models.User;
import com.onyxdb.platform.mdb.operations.models.Operation;
import com.onyxdb.platform.mdb.operations.models.OperationType;
import com.onyxdb.platform.mdb.operations.models.payload.MongoCreateUserPayload;
import com.onyxdb.platform.mdb.operations.models.payload.MongoDeleteUserPayload;
import com.onyxdb.platform.mdb.operations.repositories.OperationRepository;
import com.onyxdb.platform.mdb.projects.Project;
import com.onyxdb.platform.mdb.projects.ProjectRepository;
import com.onyxdb.platform.mdb.utils.ObjectMapperUtils;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PsmdbClient psmdbClient;
    private final TransactionTemplate transactionTemplate;
    private final OperationRepository operationRepository;
    private final ClusterRepository clusterRepository;
    private final ObjectMapper objectMapper;
    private final ProjectRepository projectRepository;

    public List<User> listUsers(UUID clusterId) {
        return userRepository.listUsers(clusterId, null);
    }

    public User getUser(UUID clusterId, String userName) {
        return userRepository.getUser(clusterId, userName);
    }

    public UUID createUser(CreateUser createUser) {
        Cluster cluster = clusterRepository.getClusterOrThrow(createUser.clusterId());
        Project project = projectRepository.getProjectOrThrow(cluster.projectId());

        String userPasswordSecretName = psmdbClient.applyMongoUserSecret(
                cluster.namespace(),
                project.name(),
                cluster.name(),
                createUser.userName(),
                createUser.password()
        );

        var user = userMapper.userToCreateToUser(createUser, userPasswordSecretName);
        var operation = Operation.scheduledWithPayload(
                OperationType.MONGO_CREATE_USER,
                cluster.id(),
                createUser.createdBy(),
                ObjectMapperUtils.convertToString(objectMapper, new MongoCreateUserPayload(
                        cluster.id(),
                        user.name(),
                        userPasswordSecretName,
                        cluster.namespace(),
                        createUser.permissions()
                )));

        transactionTemplate.executeWithoutResult(status -> {
            userRepository.createUser(user);
            userRepository.createPermissions(user.permissions());
            operationRepository.createOperation(operation);
        });

        return operation.id();
    }

    public UUID deleteUser(UUID clusterId, String userName, UUID deletedBy) {
        User user = getUser(clusterId, userName);

        var operation = Operation.scheduledWithPayload(
                OperationType.MONGO_DELETE_USER,
                user.clusterId(),
                deletedBy,
                ObjectMapperUtils.convertToString(objectMapper, new MongoDeleteUserPayload(
                        user.clusterId(),
                        user.id(),
                        user.name(),
                        deletedBy
                ))
        );
        operationRepository.createOperation(operation);

        return operation.id();
    }
}
