package com.onyxdb.platform.mdb.clusters;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.mdb.databases.DatabaseMapper;
import com.onyxdb.platform.mdb.databases.DatabaseRepository;
import com.onyxdb.platform.mdb.exceptions.ClusterNotFoundException;
import com.onyxdb.platform.mdb.hosts.HostRepository;
import com.onyxdb.platform.mdb.models.Cluster;
import com.onyxdb.platform.mdb.models.ClusterConfig;
import com.onyxdb.platform.mdb.models.ClusterToCreate;
import com.onyxdb.platform.mdb.models.DatabaseToCreate;
import com.onyxdb.platform.mdb.models.Host;
import com.onyxdb.platform.mdb.models.MongoPermissionToCreate;
import com.onyxdb.platform.mdb.models.MongoRole;
import com.onyxdb.platform.mdb.models.UpdateCluster;
import com.onyxdb.platform.mdb.models.UserToCreate;
import com.onyxdb.platform.mdb.processing.models.Operation;
import com.onyxdb.platform.mdb.processing.models.OperationType;
import com.onyxdb.platform.mdb.processing.models.TaskStatus;
import com.onyxdb.platform.mdb.processing.models.payloads.ClusterPayload;
import com.onyxdb.platform.mdb.processing.models.payloads.MongoCreateClusterPayload;
import com.onyxdb.platform.mdb.processing.models.payloads.MongoScaleClusterPayload;
import com.onyxdb.platform.mdb.processing.repositories.OperationRepository;
import com.onyxdb.platform.mdb.processing.repositories.TaskRepository;
import com.onyxdb.platform.mdb.users.UserMapper;
import com.onyxdb.platform.mdb.users.UserRepository;
import com.onyxdb.platform.mdb.utils.Consts;
import com.onyxdb.platform.mdb.utils.ObjectMapperUtils;

import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_NAMESPACE;
import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_PROJECT;

/**
 * @author foxleren
 */
@Service
@RequiredArgsConstructor
public class ClusterService {
    private static final Logger logger = LoggerFactory.getLogger(ClusterService.class);

    private final ClusterMapper clusterMapper;
    private final ClusterRepository clusterRepository;
    private final TransactionTemplate transactionTemplate;
    private final OperationRepository operationRepository;
    private final TaskRepository taskRepository;
    private final DatabaseRepository databaseRepository;
    private final DatabaseMapper databaseMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PsmdbClient psmdbClient;
    private final HostRepository hostRepository;
    private final ObjectMapper objectMapper;

    public List<Cluster> listClusters() {
        return clusterRepository.listClusters();
    }

    public UUID createCluster(ClusterToCreate clusterToCreate) {
        Cluster cluster = clusterMapper.createClusterToCluster(clusterToCreate);

        String userPasswordSecretName = psmdbClient.applyMongoUserSecret(
                DEFAULT_NAMESPACE,
                DEFAULT_PROJECT,
                cluster.name(),
                clusterToCreate.user(),
                clusterToCreate.password()
        );

        var database = databaseMapper.databaseToCreateToDatabase(new DatabaseToCreate(
                cluster.id(),
                clusterToCreate.databaseName(),
                Consts.USER_ID
        ));

        List<MongoRole> userRoles = List.of(MongoRole.READ, MongoRole.READ_WRITE);
        var userToCreate = new UserToCreate(
                cluster.id(),
                clusterToCreate.user(),
                clusterToCreate.password(),
                List.of(new MongoPermissionToCreate(
                        database.id(),
                        userRoles
                ))
        );
        var user = userMapper.userToCreateToUser(userToCreate, userPasswordSecretName);

        List<Host> hosts = psmdbClient.calculatePsmdbHostnames(
                        DEFAULT_PROJECT,
                        cluster.name(),
                        cluster.config().replicas()
                )
                .stream()
                .map(podName -> new Host(podName, cluster.id()))
                .toList();

        var operationPayload = new MongoCreateClusterPayload(
                cluster.id(),
                database.id(),
                database.name(),
                user.name(),
                userPasswordSecretName,
                DEFAULT_NAMESPACE,
                userRoles
        );
        var operation = Operation.scheduledWithPayload(
                OperationType.MONGO_CREATE_CLUSTER,
                cluster.id(),
                ObjectMapperUtils.convertToString(objectMapper, operationPayload)
        );

        transactionTemplate.executeWithoutResult(status -> {
            clusterRepository.createCluster(cluster);
            hostRepository.upsertHosts(hosts);
            databaseRepository.createDatabase(database);
            userRepository.createUser(user);
            userRepository.createPermissions(user.permissions());
            operationRepository.createOperation(operation);
        });

        return cluster.id();
    }

    public Optional<Cluster> getClusterO(UUID clusterId) {
        return clusterRepository.getClusterO(clusterId);
    }

    public Cluster getCluster(UUID clusterId) {
        return getClusterO(clusterId).orElseThrow(() -> new ClusterNotFoundException(clusterId));
    }

    public void updateTask(
            UUID taskId,
            TaskStatus status,
            int attemptsLeft,
            LocalDateTime scheduledAt
    ) {
        taskRepository.updateTask(
                taskId,
                status,
                attemptsLeft,
                scheduledAt
        );
    }

    public UUID updateCluster(UpdateCluster updateCluster) {
        Cluster cluster = getCluster(updateCluster.id());

        transactionTemplate.executeWithoutResult(status -> {
            clusterRepository.updateCluster(updateCluster);
            if (isClusterConfigChanged(cluster.config(), updateCluster.config())) {
                logger.info("Cluster config is changed. Creating operation");
                logger.info("" + cluster.config());
                logger.info("" + updateCluster.config());
                var operation = Operation.scheduledWithPayload(
                        OperationType.MONGO_SCALE_CLUSTER,
                        updateCluster.id(),
                        ObjectMapperUtils.convertToString(objectMapper, new MongoScaleClusterPayload(
                                cluster.id(),
                                updateCluster.config()
                        ))
                );
                operationRepository.createOperation(operation);
            }
        });

        // todo don't return uuid of operation, because we might don't need operation
        return UUID.randomUUID();
    }

    public UUID deleteCluster(UUID clusterId) {
        Cluster cluster = getCluster(clusterId);

        var operation = Operation.scheduledWithPayload(
                OperationType.MONGO_DELETE_CLUSTER,
                cluster.id(),
                ObjectMapperUtils.convertToString(objectMapper, new ClusterPayload(
                        clusterId
                ))
        );
        operationRepository.createOperation(operation);

        return operation.id();
    }

    private boolean isClusterConfigChanged(ClusterConfig current, ClusterConfig requested) {
        return current.replicas() != requested.replicas() ||
                !current.resources().presetId().equals(requested.resources().presetId()) ||
                !current.resources().storageClass().equals(requested.resources().storageClass()) ||
                current.resources().storage() != requested.resources().storage();
    }
}
