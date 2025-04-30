package com.onyxdb.platform.mdb.processing.consumers.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.models.Cluster;
import com.onyxdb.platform.mdb.processing.consumers.ClusterTaskProcessor;
import com.onyxdb.platform.mdb.processing.models.Task;
import com.onyxdb.platform.mdb.processing.models.TaskProcessingResult;
import com.onyxdb.platform.mdb.processing.models.TaskType;
import com.onyxdb.platform.mdb.processing.models.payloads.ClusterPayload;

import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_NAMESPACE;
import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_PROJECT;

public class MongoDeleteVectorConfigTaskProcessor extends ClusterTaskProcessor {
    private final PsmdbClient psmdbClient;

    public MongoDeleteVectorConfigTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient
    ) {
        super(objectMapper, clusterService);
        this.psmdbClient = psmdbClient;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_DELETE_VECTOR_CONFIG;
    }

    @Override
    protected TaskProcessingResult internalProcess(Task task, ClusterPayload payload) {
        Cluster cluster = clusterService.getCluster(payload.clusterId());

        psmdbClient.deleteVectorConfig(
                DEFAULT_NAMESPACE,
                DEFAULT_PROJECT,
                cluster.name()
        );

        return TaskProcessingResult.success();
    }
}
