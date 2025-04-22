package com.onyxdb.platform.core.clusters;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.core.clusters.mappers.DatabaseMapper;
import com.onyxdb.platform.core.clusters.mappers.UserMapper;
import com.onyxdb.platform.core.clusters.models.Cluster;
import com.onyxdb.platform.core.clusters.models.ClusterConfig;
import com.onyxdb.platform.core.clusters.models.CreateCluster;
import com.onyxdb.platform.core.clusters.models.Database;
import com.onyxdb.platform.core.clusters.models.DatabaseToCreate;
import com.onyxdb.platform.core.clusters.models.UpdateCluster;
import com.onyxdb.platform.core.clusters.models.User;
import com.onyxdb.platform.core.clusters.models.UserToCreate;
import com.onyxdb.platform.core.clusters.repositories.ClusterRepository;
import com.onyxdb.platform.core.clusters.repositories.DatabaseRepository;
import com.onyxdb.platform.core.clusters.repositories.UserRepository;
import com.onyxdb.platform.exceptions.BadRequestException;
import com.onyxdb.platform.taskProcessing.TaskProcessingUtils;
import com.onyxdb.platform.taskProcessing.generators.mongo.MongoCreateClusterTaskGenerator;
import com.onyxdb.platform.taskProcessing.generators.mongo.MongoDeleteClusterTaskGenerator;
import com.onyxdb.platform.taskProcessing.generators.mongo.MongoScaleHostsTaskGenerator;
import com.onyxdb.platform.taskProcessing.models.Operation;
import com.onyxdb.platform.taskProcessing.models.OperationType;
import com.onyxdb.platform.taskProcessing.models.Task;
import com.onyxdb.platform.taskProcessing.models.TaskStatus;
import com.onyxdb.platform.taskProcessing.models.TaskWithBlockers;
import com.onyxdb.platform.taskProcessing.models.payloads.ClusterTaskPayload;
import com.onyxdb.platform.taskProcessing.repositories.OperationRepository;
import com.onyxdb.platform.taskProcessing.repositories.TaskRepository;

/**
 * @author foxleren
 */
public class ClusterService {
    private final ClusterMapper clusterMapper;
    private final ClusterRepository clusterRepository;
    private final TransactionTemplate transactionTemplate;
    private final OperationRepository operationRepository;
    private final TaskRepository taskRepository;
    private final MongoCreateClusterTaskGenerator mongoCreateClusterTaskGenerator;
    private final MongoScaleHostsTaskGenerator mongoScaleHostsTaskGenerator;
    private final MongoDeleteClusterTaskGenerator mongoDeleteClusterTaskGenerator;
    private final DatabaseRepository databaseRepository;
    private final DatabaseMapper databaseMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public ClusterService(
            ClusterMapper clusterMapper,
            ClusterRepository clusterRepository,
            TransactionTemplate transactionTemplate,
            OperationRepository operationRepository,
            TaskRepository taskRepository,
            MongoCreateClusterTaskGenerator mongoCreateClusterTaskGenerator,
            MongoScaleHostsTaskGenerator mongoScaleHostsTaskGenerator,
            MongoDeleteClusterTaskGenerator mongoDeleteClusterTaskGenerator,
            DatabaseRepository databaseRepository,
            DatabaseMapper databaseMapper,
            UserRepository userRepository,
            UserMapper userMapper
    ) {
        this.clusterMapper = clusterMapper;
        this.clusterRepository = clusterRepository;
        this.transactionTemplate = transactionTemplate;
        this.operationRepository = operationRepository;
        this.taskRepository = taskRepository;
        this.mongoCreateClusterTaskGenerator = mongoCreateClusterTaskGenerator;
        this.mongoScaleHostsTaskGenerator = mongoScaleHostsTaskGenerator;
        this.mongoDeleteClusterTaskGenerator = mongoDeleteClusterTaskGenerator;
        this.databaseRepository = databaseRepository;
        this.databaseMapper = databaseMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<Cluster> listClusters() {
        return clusterRepository.listClusters();
    }

    public UUID createCluster(CreateCluster createCluster) {
        Cluster cluster = clusterMapper.createClusterToCluster(createCluster);

        // TODO create table cluster_id to operation_id
        var operation = Operation.scheduled(OperationType.MONGODB_CREATE_CLUSTER);
        List<TaskWithBlockers> tasksWithBlockers = mongoCreateClusterTaskGenerator.generateTasks(
                operation.id(),
                new ClusterTaskPayload(cluster.id())
        );

        transactionTemplate.executeWithoutResult(status -> {
            clusterRepository.createCluster(cluster);
            createOperationWithTasks(operation, tasksWithBlockers);
        });

        return cluster.id();
    }

    public Optional<Cluster> getClusterO(UUID clusterId) {
        return clusterRepository.getClusterO(clusterId);
    }

    public Cluster getCluster(UUID clusterId) {
        return getClusterO(clusterId).orElseThrow();
    }

    public void validateClusterExistence(UUID clusterId) {
        getCluster(clusterId);
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

    public UUID scaleHosts(UUID clusterId, int replicas) {
        Cluster cluster = getCluster(clusterId);

        var updatedConfig = ClusterConfig.builder()
                .copyFrom(cluster.config())
                .withReplicas(replicas)
                .build();

        var operation = Operation.scheduled(OperationType.MONGODB_SCALE_HOSTS);
        List<TaskWithBlockers> tasksWithBlockers = mongoCreateClusterTaskGenerator.generateTasks(
                operation.id(),
                new ClusterTaskPayload(cluster.id())
        );

        transactionTemplate.executeWithoutResult(status -> {
            clusterRepository.updateClusterConfig(clusterId, updatedConfig);
            createOperationWithTasks(operation, tasksWithBlockers);
        });

        return operation.id();
    }

    public UUID updateCluster(UpdateCluster updateCluster) {
        validateClusterExistence(updateCluster.id());

        var operation = Operation.scheduled(OperationType.MONGODB_SCALE_HOSTS);
//        List<TaskWithBlockers> tasksWithBlockers = mongoDeleteClusterTaskGenerator.generateClusterTasks(
//                operation.id(),
//                operation.type(),
//                updateCluster.id()
//        );
//        List<Task> tasks = TaskProcessingUtils.getTasksFromTasksWithBlockers(tasksWithBlockers);
//
//        transactionTemplate.executeWithoutResult(status -> {
//            clusterRepository.updateClusterConfig(clusterId, updatedConfig);
//            createOperationWithTasks(operation, tasks, tasksWithBlockers);
//        });

        return operation.id();
    }

    public UUID deleteCluster(UUID clusterId) {
        Cluster cluster = getCluster(clusterId);

        var operation = Operation.scheduled(OperationType.MONGODB_DELETE_CLUSTER);
        List<TaskWithBlockers> tasksWithBlockers = mongoDeleteClusterTaskGenerator.generateTasks(
                operation.id(),
                new ClusterTaskPayload(cluster.id())
        );

        transactionTemplate.executeWithoutResult(status -> {
            clusterRepository.markClusterDeleted(clusterId);
            createOperationWithTasks(operation, tasksWithBlockers);
        });

        return operation.id();
    }

    private void createOperationWithTasks(Operation operation, List<TaskWithBlockers> tasksWithBlockers) {
        List<Task> tasks = TaskProcessingUtils.getTasksFromTasksWithBlockers(tasksWithBlockers);
        operationRepository.create(operation);
        taskRepository.createBulk(tasks);
        taskRepository.createBlockerTasksBulk(tasksWithBlockers);
    }

    public List<Database> listDatabases(UUID clusterId) {
        return databaseRepository.listDatabases(clusterId);
    }

    public UUID createDatabase(DatabaseToCreate databaseToCreate) {
        databaseRepository.createDatabase(databaseMapper.map(databaseToCreate));
        return UUID.randomUUID();
    }

    public UUID deleteDatabase(UUID clusterId, UUID databaseId) {
        databaseRepository.getDatabaseO(clusterId, databaseId)
                .orElseThrow(() -> new BadRequestException(String.format(
                        "Can't find database with id '%s'", databaseId
                )));

        databaseRepository.markDatabaseAsDeleted(clusterId, databaseId);
        return UUID.randomUUID();
    }

    public List<User> listUsers(UUID clusterId) {
        return userRepository.listUsers(clusterId, null);
    }

    public User getUser(UUID userId) {
        return userRepository.getUserO(userId)
                .orElseThrow(() -> new BadRequestException(String.format(
                        "Can't find user with id '%s'", userId
                )));
    }

    public UUID createUser(UserToCreate userToCreate) {
        User user = userMapper.map(userToCreate);

        transactionTemplate.executeWithoutResult(status -> {
            userRepository.createUser(user);
            userRepository.createPermissions(user.permissions());
        });

        return UUID.randomUUID();
    }

    public UUID deleteUser(UUID userId) {
        getUser(userId);

        transactionTemplate.executeWithoutResult(status -> {
            userRepository.markUserAsDeleted(userId);
            userRepository.markPermissionsAsDeleted(userId);
        });

        return UUID.randomUUID();
    }
}
