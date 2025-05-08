package com.onyxdb.platform.mdb.operations.consumers.mongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.clusters.ClusterRepository;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.clusters.models.ClusterStatus;
import com.onyxdb.platform.mdb.operations.consumers.TaskConsumer;
import com.onyxdb.platform.mdb.operations.models.Task;
import com.onyxdb.platform.mdb.operations.models.TaskResult;
import com.onyxdb.platform.mdb.operations.models.TaskType;
import com.onyxdb.platform.mdb.operations.models.payload.MongoDeleteClusterPayload;

@Component
public class MongoMarkClusterDeletingTaskConsumer extends TaskConsumer<MongoDeleteClusterPayload> {
    private final ClusterRepository clusterRepository;

    public MongoMarkClusterDeletingTaskConsumer(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            ClusterRepository clusterRepository
    ) {
        super(objectMapper, clusterService);
        this.clusterRepository = clusterRepository;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_MARK_CLUSTER_DELETING;
    }

    @Override
    protected TaskResult internalProcess(Task task, MongoDeleteClusterPayload payload) {
        Cluster cluster = clusterService.getClusterOrThrow(payload.clusterId());

        Cluster updatedCluster = Cluster.builder()
                .copy(cluster)
                .status(ClusterStatus.DELETING)
                .build();
        clusterRepository.updateCluster(updatedCluster);

        return TaskResult.success();
    }

    @Override
    protected MongoDeleteClusterPayload parsePayload(Task task) throws JsonProcessingException {
        return objectMapper.readValue(task.payload(), MongoDeleteClusterPayload.class);
    }
}
