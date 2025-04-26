package com.onyxdb.platform.mdb.users;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.core.clusters.models.Cluster;
import com.onyxdb.platform.core.clusters.models.User;
import com.onyxdb.platform.core.clusters.models.UserToCreate;
import com.onyxdb.platform.mdb.clusters.ClusterRepository;
import com.onyxdb.platform.processing.models.Operation;
import com.onyxdb.platform.processing.models.OperationType;
import com.onyxdb.platform.processing.models.payloads.MongoCreateUserPayload;
import com.onyxdb.platform.processing.models.payloads.MongoDeleteUserPayload;
import com.onyxdb.platform.processing.repositories.OperationRepository;
import com.onyxdb.platform.utils.ObjectMapperUtils;

import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_NAMESPACE;
import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_PROJECT;

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

    public List<User> listUsers(UUID clusterId) {
        return userRepository.listUsers(clusterId, null);
    }

    public User getUser(UUID userId) {
        return userRepository.getUser(userId);
    }

    public UUID createUser(UserToCreate userToCreate) {
        Cluster cluster = clusterRepository.getCluster(userToCreate.clusterId());

        String userPasswordSecretName = psmdbClient.applyMongoUserSecret(
                DEFAULT_NAMESPACE,
                DEFAULT_PROJECT,
                cluster.name(),
                userToCreate.username(),
                userToCreate.password()
        );

        var user = userMapper.userToCreateToUser(userToCreate, userPasswordSecretName);
        var operation = Operation.scheduledWithPayload(
                OperationType.MONGO_CREATE_USER,
                cluster.id(),
                ObjectMapperUtils.convertToString(objectMapper, new MongoCreateUserPayload(
                        cluster.id(),
                        user.name(),
                        userPasswordSecretName,
                        DEFAULT_NAMESPACE,
                        userToCreate.permissions()
                )));

        transactionTemplate.executeWithoutResult(status -> {
            userRepository.createUser(user);
            userRepository.createPermissions(user.permissions());
            operationRepository.createOperation(operation);
        });

        return operation.id();
    }

    public UUID deleteUser(UUID userId) {
        User user = getUser(userId);

        var operation = Operation.scheduledWithPayload(
                OperationType.MONGO_DELETE_USER,
                user.clusterId(),
                ObjectMapperUtils.convertToString(objectMapper, new MongoDeleteUserPayload(
                        user.clusterId(),
                        user.id(),
                        user.name()
                ))
        );
        operationRepository.createOperation(operation);

        return operation.id();
    }
}
