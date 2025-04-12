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

import com.onyxdb.mdb.core.clusters.models.Cluster;
import com.onyxdb.mdb.core.clusters.repositories.ClusterRepository;
import com.onyxdb.mdb.taskProcessing.generators.CompositeTaskGenerator;
import com.onyxdb.mdb.taskProcessing.models.OperationStatus;
import com.onyxdb.mdb.taskProcessing.models.Task;
import com.onyxdb.mdb.taskProcessing.models.TaskStatus;
import com.onyxdb.mdb.taskProcessing.repositories.OperationRepository;
import com.onyxdb.mdb.taskProcessing.repositories.TaskRepository;

/**
 * @author foxleren
 */
@Service
@RequiredArgsConstructor
public class ClusterServiceOld implements BaseClusterService {
    private final ClusterRepository clusterRepository;
    private final OperationRepository operationRepository;
    private final TaskRepository taskRepository;
    private final CompositeTaskGenerator clusterTasksGenerator;
    private final TransactionTemplate transactionTemplate;

    @Override
    public Optional<Cluster> getByIdO(UUID id) {
//        return clusterRepository.getByIdO(id);
        return Optional.empty();
    }

    @Override
    public void updateTaskStatus(UUID taskId, TaskStatus taskStatus) {
        taskRepository.updateStatus(taskId, taskStatus);
    }

    @Override
    public void updateOperationStatus(UUID operationId, OperationStatus operationStatus) {
        operationRepository.updateStatus(operationId, operationStatus);
    }

    @Override
    public void updateTaskAndOperationStatus(
            UUID taskId,
            TaskStatus taskStatus,
            @Nullable
            Integer attemptsLeft,
            UUID operationId,
            OperationStatus operationStatus
    ) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                taskRepository.updateTask(taskId, taskStatus, attemptsLeft, null);
                taskRepository.updateStatus(taskId, taskStatus);
                operationRepository.updateStatus(operationId, operationStatus);
            }
        });
    }

    @Override
    public List<Task> getTasksToProcess(int limit, LocalDateTime scheduledAt) {
        return taskRepository.getTasksToProcess(limit, scheduledAt);
    }

    @Override
    public void updateProject(UUID clusterId, UUID projectId) {
//        clusterRepository.updateProject(clusterId, projectId);
    }
}
