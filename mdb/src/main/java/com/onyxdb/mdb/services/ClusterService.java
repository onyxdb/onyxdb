package com.onyxdb.mdb.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.mdb.generators.CompositeClusterTasksGenerator;
import com.onyxdb.mdb.models.Cluster;
import com.onyxdb.mdb.models.ClusterOperation;
import com.onyxdb.mdb.models.ClusterOperationStatus;
import com.onyxdb.mdb.models.ClusterOperationType;
import com.onyxdb.mdb.models.ClusterTask;
import com.onyxdb.mdb.models.ClusterTaskStatus;
import com.onyxdb.mdb.models.ClusterTaskWithBlockers;
import com.onyxdb.mdb.models.ClusterToCreate;
import com.onyxdb.mdb.repositories.ClusterOperationRepository;
import com.onyxdb.mdb.repositories.ClusterRepository;
import com.onyxdb.mdb.repositories.ClusterTaskRepository;

/**
 * @author foxleren
 */
@Service
@RequiredArgsConstructor
public class ClusterService implements BaseClusterService {
    private final ClusterRepository clusterRepository;
    private final ClusterOperationRepository clusterOperationRepository;
    private final ClusterTaskRepository clusterTaskRepository;
    private final CompositeClusterTasksGenerator clusterTasksGenerator;
    private final TransactionTemplate transactionTemplate;

    @Override
    public UUID create(UUID createdBy, ClusterToCreate clusterToCreate) {
//        var cluster = Cluster.fromClusterToCreate(clusterToCreate);
//        var clusterOperation = ClusterOperation.scheduled(
//                cluster.id(),
//                ClusterOperationType.CREATE_CLUSTER,
//                createdBy
//        );
//        List<ClusterTaskWithBlockers> clusterTasksWithBlockers = clusterTasksGenerator.generateTasks(
//                cluster.id(),
//                cluster.type(),
//                clusterOperation.id(),
//                clusterOperation.type()
//        );
//
//        List<ClusterTask> clusterTasks = new ArrayList<>(clusterTasksWithBlockers.size());
//        Map<UUID, List<UUID>> taskIdToBlockerIds = new HashMap<>();
//        for (var clusterTaskWithBlockers : clusterTasksWithBlockers) {
//            clusterTasks.add(clusterTaskWithBlockers.task());
//            taskIdToBlockerIds.put(clusterTaskWithBlockers.task().id(), clusterTaskWithBlockers.blockerIds());
//        }
//
//        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
//            @Override
//            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
//                clusterRepository.create(cluster);
//                clusterOperationRepository.create(clusterOperation);
//                clusterTaskRepository.createBulk(clusterTasks);
//                clusterTaskRepository.createBlockerTasksBulk(taskIdToBlockerIds);
//            }
//        });
//
//        return cluster.id();
        return null;
    }

    @Override
    public Optional<Cluster> getByIdO(UUID id) {
        return clusterRepository.getByIdO(id);
    }

    @Override
    public void updateTaskStatus(UUID taskId, ClusterTaskStatus taskStatus) {
        clusterTaskRepository.updateStatus(taskId, taskStatus);
    }

    @Override
    public void updateOperationStatus(UUID operationId, ClusterOperationStatus operationStatus) {
        clusterOperationRepository.updateStatus(operationId, operationStatus);
    }

    @Override
    public void updateTaskAndOperationStatus(
            UUID taskId,
            ClusterTaskStatus taskStatus,
            UUID operationId,
            ClusterOperationStatus operationStatus
    ) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                clusterTaskRepository.updateStatus(taskId, taskStatus);
                clusterOperationRepository.updateStatus(operationId, operationStatus);
            }
        });
    }

    @Override
    public List<ClusterTask> getTasksToProcess(int limit) {
        return clusterTaskRepository.getTasksToProcess(limit);
    }
}
