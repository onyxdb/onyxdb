package com.onyxdb.platform.mdb.clusters;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.clusters.models.ClusterFilter;
import com.onyxdb.platform.mdb.clusters.models.CreateCluster;
import com.onyxdb.platform.mdb.clusters.models.CreateClusterResult;
import com.onyxdb.platform.mdb.clusters.models.CreateDatabase;
import com.onyxdb.platform.mdb.clusters.models.CreateMongoPermission;
import com.onyxdb.platform.mdb.clusters.models.CreateUserWithSecret;
import com.onyxdb.platform.mdb.clusters.models.Host;
import com.onyxdb.platform.mdb.clusters.models.MongoRole;
import com.onyxdb.platform.mdb.clusters.models.UpdateCluster;
import com.onyxdb.platform.mdb.databases.DatabaseMapper;
import com.onyxdb.platform.mdb.databases.DatabaseRepository;
import com.onyxdb.platform.mdb.hosts.HostMapper;
import com.onyxdb.platform.mdb.hosts.HostRepository;
import com.onyxdb.platform.mdb.operations.models.Operation;
import com.onyxdb.platform.mdb.operations.models.OperationType;
import com.onyxdb.platform.mdb.operations.models.payload.MongoCreateClusterPayload;
import com.onyxdb.platform.mdb.operations.models.payload.MongoDeleteClusterPayload;
import com.onyxdb.platform.mdb.operations.models.payload.MongoModifyClusterPayload;
import com.onyxdb.platform.mdb.operations.repositories.OperationRepository;
import com.onyxdb.platform.mdb.operations.repositories.TaskRepository;
import com.onyxdb.platform.mdb.projects.Project;
import com.onyxdb.platform.mdb.projects.ProjectService;
import com.onyxdb.platform.mdb.users.UserMapper;
import com.onyxdb.platform.mdb.users.UserRepository;
import com.onyxdb.platform.mdb.utils.ObjectMapperUtils;
import com.onyxdb.platform.mdb.utils.OnyxdbConsts;

/**
 * @author foxleren
 */
@Service
@RequiredArgsConstructor
public class ClusterService {
    private static final Logger logger = LoggerFactory.getLogger(ClusterService.class);

    private final ProjectService projectService;
    private final ClusterMapper clusterMapper;
    private final ClusterConfigValidator clusterConfigValidator;

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
    private final HostMapper hostMapper;

    public List<Cluster> listClusters(ClusterFilter filter) {
        return clusterRepository.listClusters(filter);
    }

    public Cluster getClusterOrThrow(UUID clusterId) {
        return clusterRepository.getClusterOrThrow(clusterId);
    }

    public CreateClusterResult createCluster(CreateCluster createCluster) {
        Project project = projectService.getUndeletedProjectOrThrow(createCluster.projectId());
        // TODO get namespace from project
        String namespace = OnyxdbConsts.NAMESPACE;

        clusterConfigValidator.validate(createCluster.config());

        Cluster cluster = clusterMapper.createClusterToCluster(createCluster, namespace);

        var createDatabase = new CreateDatabase(
                createCluster.databaseName(),
                cluster.id(),
                createCluster.createdBy()
        );
        var database = databaseMapper.databaseToCreateToDatabase(createDatabase);

        String userPasswordSecretName = psmdbClient.applyMongoUserSecret(
                namespace,
                project.name(),
                cluster.name(),
                createCluster.userName(),
                createCluster.password()
        );

        List<MongoRole> userRoles = List.of(MongoRole.READ_WRITE);
        var createUser = new CreateUserWithSecret(
                createCluster.userName(),
                userPasswordSecretName,
                cluster.id(),
                List.of(new CreateMongoPermission(
                        createCluster.userName(),
                        database.name(),
                        cluster.id(),
                        userRoles
                )),
                createCluster.createdBy()
        );
        var user = userMapper.createUserWithSecretToUser(createUser);

        List<Host> hosts = hostMapper.hostNamesToHosts(PsmdbClient.calculatePsmdbHostNames(
                project.name(),
                cluster.name(),
                cluster.config().replicas()
        ), cluster.id());

        var operationPayload = new MongoCreateClusterPayload(
                cluster.id(),
                database.name(),
                user.name(),
                userPasswordSecretName,
                namespace,
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

        return new CreateClusterResult(
                cluster.id(),
                operation.id()
        );
    }

    public UUID updateCluster(UpdateCluster updateCluster) {
        Cluster cluster = getClusterOrThrow(updateCluster.id());

        var operation = Operation.scheduledWithPayload(
                OperationType.MONGO_MODIFY_CLUSTER,
                updateCluster.id(),
                ObjectMapperUtils.convertToString(objectMapper, new MongoModifyClusterPayload(
                        cluster.id(),
                        updateCluster.config()
                ))
        );
        var updatedCluster = Cluster.builder()
                .copy(cluster)
                .overrideWithUpdateCluster(updateCluster)
                .build();
        transactionTemplate.executeWithoutResult(status -> {
            clusterRepository.updateCluster(updatedCluster);
            operationRepository.createOperation(operation);
        });

        return operation.id();
    }

    public UUID deleteCluster(UUID clusterId, UUID deletedBy) {
        Cluster cluster = getClusterOrThrow(clusterId);

        var operation = Operation.scheduledWithPayload(
                OperationType.MONGO_DELETE_CLUSTER,
                cluster.id(),
                ObjectMapperUtils.convertToString(objectMapper, new MongoDeleteClusterPayload(
                        clusterId,
                        deletedBy
                ))
        );
        operationRepository.createOperation(operation);

        return operation.id();
    }
}
