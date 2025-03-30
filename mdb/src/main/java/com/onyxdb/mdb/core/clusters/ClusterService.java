package com.onyxdb.mdb.core.clusters;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.mdb.core.clusters.generators.CompositeClusterTasksGenerator;
import com.onyxdb.mdb.core.clusters.models.Cluster;
import com.onyxdb.mdb.core.clusters.models.ClusterOperation;
import com.onyxdb.mdb.core.clusters.models.ClusterOperationType;
import com.onyxdb.mdb.core.clusters.models.ClusterTask;
import com.onyxdb.mdb.core.clusters.models.ClusterTaskStatus;
import com.onyxdb.mdb.core.clusters.models.ClusterTaskWithBlockers;
import com.onyxdb.mdb.core.clusters.models.CreateCluster;
import com.onyxdb.mdb.core.clusters.repositories.ClusterRepository;
import com.onyxdb.mdb.repositories.ClusterOperationRepository;
import com.onyxdb.mdb.repositories.ClusterTaskRepository;

/**
 * @author foxleren
 */
public class ClusterService {
    private final ClusterMapper clusterMapper;
    private final ClusterRepository clusterRepository;
    private final TransactionTemplate transactionTemplate;
    private final CompositeClusterTasksGenerator clusterTasksGenerator;
    private final ClusterOperationRepository clusterOperationRepository;
    private final ClusterTaskRepository clusterTaskRepository;

    public ClusterService(
            ClusterMapper clusterMapper,
            ClusterRepository clusterRepository,
            TransactionTemplate transactionTemplate,
            CompositeClusterTasksGenerator clusterTasksGenerator,
            ClusterOperationRepository clusterOperationRepository,
            ClusterTaskRepository clusterTaskRepository
    ) {
        this.clusterMapper = clusterMapper;
        this.clusterRepository = clusterRepository;
        this.transactionTemplate = transactionTemplate;
        this.clusterTasksGenerator = clusterTasksGenerator;
        this.clusterOperationRepository = clusterOperationRepository;
        this.clusterTaskRepository = clusterTaskRepository;
    }

    public UUID createCluster(CreateCluster createCluster) {
        Cluster cluster = clusterMapper.createClusterToCluster(createCluster);
        var createClusterOperation = ClusterOperation.scheduled(
                cluster.id(),
                cluster.type(),
                ClusterOperationType.CREATE_CLUSTER
        );

        List<ClusterTaskWithBlockers> clusterTasksWithBlockers = clusterTasksGenerator.generateTasks(
                cluster.id(),
                cluster.type(),
                createClusterOperation.id(),
                createClusterOperation.type()
        );

        List<ClusterTask> clusterTasks = new ArrayList<>(clusterTasksWithBlockers.size());
        Map<UUID, List<UUID>> taskIdToBlockerIds = new HashMap<>();
        for (var clusterTaskWithBlockers : clusterTasksWithBlockers) {
            clusterTasks.add(clusterTaskWithBlockers.task());
            taskIdToBlockerIds.put(clusterTaskWithBlockers.task().id(), clusterTaskWithBlockers.blockerIds());
        }

        transactionTemplate.executeWithoutResult(status -> {
            clusterRepository.createCluster(cluster);
            clusterOperationRepository.create(createClusterOperation);
            clusterTaskRepository.createBulk(clusterTasks);
            clusterTaskRepository.createBlockerTasksBulk(taskIdToBlockerIds);
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
            ClusterTaskStatus status,
            int attemptsLeft,
            LocalDateTime scheduledAt
    ) {
        clusterTaskRepository.updateTask(
                taskId,
                status,
                attemptsLeft,
                scheduledAt
        );
    }
}
