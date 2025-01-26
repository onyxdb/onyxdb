package com.onyxdb.mdb.services;

import java.util.List;
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
import com.onyxdb.mdb.models.ClusterOperationType;
import com.onyxdb.mdb.models.ClusterTask;
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
    public UUID create(ClusterToCreate clusterToCreate) {
        var cluster = Cluster.fromClusterToCreate(clusterToCreate);
        var clusterOperation = ClusterOperation.scheduled(
                cluster.id(),
                ClusterOperationType.CREATE_CLUSTER,
                UUID.randomUUID()
        );
        List<ClusterTask> clusterTasks = clusterTasksGenerator.generateTasks(
                cluster.id(),
                cluster.type(),
                clusterOperation.id(),
                ClusterOperationType.CREATE_CLUSTER
        );

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                clusterRepository.create(cluster);
                clusterOperationRepository.create(clusterOperation);
                clusterTaskRepository.createBulk(clusterTasks);
            }
        });

        return cluster.id();
    }
}
