package com.onyxdb.mdb.core.clusters;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.mdb.core.clusters.models.Cluster;
import com.onyxdb.mdb.core.clusters.models.ClusterConfig;
import com.onyxdb.mdb.core.clusters.models.CreateCluster;
import com.onyxdb.mdb.core.clusters.repositories.ClusterRepository;
import com.onyxdb.mdb.taskProcessing.TaskProcessingUtils;
import com.onyxdb.mdb.taskProcessing.generators.CompositeTaskGenerator;
import com.onyxdb.mdb.taskProcessing.models.Operation;
import com.onyxdb.mdb.taskProcessing.models.OperationType;
import com.onyxdb.mdb.taskProcessing.models.Task;
import com.onyxdb.mdb.taskProcessing.models.TaskStatus;
import com.onyxdb.mdb.taskProcessing.models.TaskWithBlockers;
import com.onyxdb.mdb.taskProcessing.repositories.OperationRepository;
import com.onyxdb.mdb.taskProcessing.repositories.TaskRepository;

/**
 * @author foxleren
 */
public class ClusterService {
    private final ClusterMapper clusterMapper;
    private final ClusterRepository clusterRepository;
    private final TransactionTemplate transactionTemplate;
    private final CompositeTaskGenerator compositeTaskGenerator;
    private final OperationRepository operationRepository;
    private final TaskRepository taskRepository;

    public ClusterService(
            ClusterMapper clusterMapper,
            ClusterRepository clusterRepository,
            TransactionTemplate transactionTemplate,
            CompositeTaskGenerator compositeTaskGenerator,
            OperationRepository operationRepository,
            TaskRepository taskRepository
    ) {
        this.clusterMapper = clusterMapper;
        this.clusterRepository = clusterRepository;
        this.transactionTemplate = transactionTemplate;
        this.compositeTaskGenerator = compositeTaskGenerator;
        this.operationRepository = operationRepository;
        this.taskRepository = taskRepository;
    }

    public UUID createCluster(CreateCluster createCluster) {
        Cluster cluster = clusterMapper.createClusterToCluster(createCluster);

        // TODO create table cluster_id to operation_id
        var operation = Operation.scheduled(OperationType.MONGODB_CREATE_CLUSTER);
        List<TaskWithBlockers> tasksWithBlockers = compositeTaskGenerator.generateClusterTasks(
                operation.id(),
                operation.type(),
                cluster.id()
        );
        List<Task> tasks = TaskProcessingUtils.getTasksFromTasksWithBlockers(tasksWithBlockers);


        transactionTemplate.executeWithoutResult(status -> {
            clusterRepository.createCluster(cluster);
            operationRepository.create(operation);
            taskRepository.createBulk(tasks);
            taskRepository.createBlockerTasksBulk(tasksWithBlockers);
        });

        return cluster.id();
    }

    public Optional<Cluster> getClusterO(UUID clusterId) {
        return clusterRepository.getClusterO(clusterId);
    }

    public Cluster getCluster(UUID clusterId) {
        return getClusterO(clusterId).orElseThrow();
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

        var updatedConfig = ClusterConfig.builder().copyFrom(cluster.config())
                .withReplicas(replicas)
                .build();
        clusterRepository.updateClusterConfig(clusterId, updatedConfig);

        var operation = Operation.scheduled(OperationType.MONGODB_SCALE_HOSTS);
        List<TaskWithBlockers> tasksWithBlockers = compositeTaskGenerator.generateClusterTasks(
                operation.id(),
                operation.type(),
                clusterId
        );
        List<Task> tasks = TaskProcessingUtils.getTasksFromTasksWithBlockers(tasksWithBlockers);

        transactionTemplate.executeWithoutResult(status -> {
            operationRepository.create(operation);
            taskRepository.createBulk(tasks);
            taskRepository.createBlockerTasksBulk(tasksWithBlockers);
        });

        return operation.id();
    }

//    public UUID deleteCluster(UUID clusterId) {
//        var cluster = getCluster(clusterId);
//
//        clusterRepository.markClusterDeleted(clusterId);
//        var operation = ClusterOperation.scheduled(
//                cluster.id(),
//                cluster.type(),
//                OperationType.MONGODB_DELETE_CLUSTER
//        );
//
//        List<TaskWithBlockers> tasksWithBlockers = compositeTaskGenerator.generateClusterTasks(
//                operation.id(),
//                operation.type(),
//                cluster.id()
//        );
//        List<Task> tasks = tasksWithBlockers.stream()
//                .map(TaskWithBlockers::task)
//                .toList();
//
//        transactionTemplate.executeWithoutResult(status -> {
//            operationRepository.create(operation);
//            taskRepository.createBulk(tasks);
//            taskRepository.createBlockerTasksBulk(tasksWithBlockers);
//        });
//
//        return operation.id();
//    }
}
