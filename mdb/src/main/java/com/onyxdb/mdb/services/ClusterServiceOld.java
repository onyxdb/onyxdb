package com.onyxdb.mdb.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.mdb.core.clusters.generators.CompositeClusterTasksGenerator;
import com.onyxdb.mdb.core.clusters.models.Cluster;
import com.onyxdb.mdb.core.clusters.models.ClusterOperationStatus;
import com.onyxdb.mdb.core.clusters.models.ClusterTask;
import com.onyxdb.mdb.core.clusters.models.ClusterTaskStatus;
import com.onyxdb.mdb.core.clusters.repositories.ClusterRepository;
import com.onyxdb.mdb.repositories.ClusterOperationRepository;
import com.onyxdb.mdb.repositories.ClusterTaskRepository;

/**
 * @author foxleren
 */
@Service
@RequiredArgsConstructor
public class ClusterServiceOld implements BaseClusterService {
    private final ClusterRepository clusterRepository;
    private final ClusterOperationRepository clusterOperationRepository;
    private final ClusterTaskRepository clusterTaskRepository;
    private final CompositeClusterTasksGenerator clusterTasksGenerator;
    private final TransactionTemplate transactionTemplate;

    @Override
    public Optional<Cluster> getByIdO(UUID id) {
//        return clusterRepository.getByIdO(id);
        return Optional.empty();
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
            @Nullable
            Integer attemptsLeft,
            UUID operationId,
            ClusterOperationStatus operationStatus
    ) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                clusterTaskRepository.updateTask(taskId, taskStatus, attemptsLeft, null);
                clusterTaskRepository.updateStatus(taskId, taskStatus);
                clusterOperationRepository.updateStatus(operationId, operationStatus);
            }
        });
    }

    @Override
    public List<ClusterTask> getTasksToProcess(int limit, LocalDateTime scheduledAt) {
        return clusterTaskRepository.getTasksToProcess(limit, scheduledAt);
    }

    @Override
    public void updateProject(UUID clusterId, UUID projectId) {
//        clusterRepository.updateProject(clusterId, projectId);
    }
}
