package com.onyxdb.platform.mdb.operations.consumers.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.mdb.clusters.ClusterRepository;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.operations.consumers.ClusterTaskConsumer;
import com.onyxdb.platform.mdb.operations.models.Task;
import com.onyxdb.platform.mdb.operations.models.TaskResult;
import com.onyxdb.platform.mdb.operations.models.TaskType;
import com.onyxdb.platform.mdb.operations.models.payload.ClusterPayload;

@Component
public class MongoMarkClusterDeletedTaskConsumer extends ClusterTaskConsumer {
    private final PsmdbClient psmdbClient;
    private final ClusterRepository clusterRepository;

    public MongoMarkClusterDeletedTaskConsumer(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient, ClusterRepository clusterRepository
    ) {
        super(objectMapper, clusterService);
        this.psmdbClient = psmdbClient;
        this.clusterRepository = clusterRepository;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_MARK_CLUSTER_DELETED;
    }

    @Override
    protected TaskResult internalProcess(Task task, ClusterPayload payload) {
        clusterRepository.markClusterDeleted(payload.clusterId());
        return TaskResult.success();
    }
}
