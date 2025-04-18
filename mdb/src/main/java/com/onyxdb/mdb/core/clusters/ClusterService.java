package com.onyxdb.mdb.core.clusters;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.mdb.core.clusters.mappers.DatabaseMapper;
import com.onyxdb.mdb.core.clusters.models.Cluster;
import com.onyxdb.mdb.core.clusters.models.ClusterConfig;
import com.onyxdb.mdb.core.clusters.models.CreateCluster;
import com.onyxdb.mdb.core.clusters.models.Database;
import com.onyxdb.mdb.core.clusters.models.DatabaseToCreate;
import com.onyxdb.mdb.core.clusters.models.UpdateCluster;
import com.onyxdb.mdb.core.clusters.repositories.ClusterRepository;
import com.onyxdb.mdb.core.clusters.repositories.DatabaseRepository;
import com.onyxdb.mdb.exceptions.BadRequestException;
import com.onyxdb.mdb.taskProcessing.TaskProcessingUtils;
import com.onyxdb.mdb.taskProcessing.generators.mongo.MongoCreateClusterTaskGenerator;
import com.onyxdb.mdb.taskProcessing.generators.mongo.MongoDeleteClusterTaskGenerator;
import com.onyxdb.mdb.taskProcessing.generators.mongo.MongoScaleHostsTaskGenerator;
import com.onyxdb.mdb.taskProcessing.models.Operation;
import com.onyxdb.mdb.taskProcessing.models.OperationType;
import com.onyxdb.mdb.taskProcessing.models.Task;
import com.onyxdb.mdb.taskProcessing.models.TaskStatus;
import com.onyxdb.mdb.taskProcessing.models.TaskWithBlockers;
import com.onyxdb.mdb.taskProcessing.models.payloads.ClusterTaskPayload;
import com.onyxdb.mdb.taskProcessing.repositories.OperationRepository;
import com.onyxdb.mdb.taskProcessing.repositories.TaskRepository;

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
            DatabaseMapper databaseMapper
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
}
