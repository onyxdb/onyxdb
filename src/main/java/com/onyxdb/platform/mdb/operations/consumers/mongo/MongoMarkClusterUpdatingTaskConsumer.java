package com.onyxdb.platform.mdb.operations.consumers.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.clusters.ClusterRepository;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.clusters.models.ClusterStatus;
import com.onyxdb.platform.mdb.operations.consumers.ClusterTaskConsumer;
import com.onyxdb.platform.mdb.operations.models.Task;
import com.onyxdb.platform.mdb.operations.models.TaskResult;
import com.onyxdb.platform.mdb.operations.models.TaskType;
import com.onyxdb.platform.mdb.operations.models.payload.ClusterPayload;

@Component
public class MongoMarkClusterUpdatingTaskConsumer extends ClusterTaskConsumer {
    private final ClusterRepository clusterRepository;

    public MongoMarkClusterUpdatingTaskConsumer(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            ClusterRepository clusterRepository
    ) {
        super(objectMapper, clusterService);
        this.clusterRepository = clusterRepository;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_MARK_CLUSTER_UPDATING;
    }

    @Override
    protected TaskResult internalProcess(Task task, ClusterPayload payload) {
        Cluster cluster = clusterService.getClusterOrThrow(payload.clusterId());

        Cluster updatedCluster = Cluster.builder()
                .copy(cluster)
                .status(ClusterStatus.UPDATING)
                .build();
        clusterRepository.updateCluster(updatedCluster);

        return TaskResult.success();
    }
}
