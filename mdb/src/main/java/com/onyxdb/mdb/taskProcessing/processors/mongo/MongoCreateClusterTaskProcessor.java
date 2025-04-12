package com.onyxdb.mdb.taskProcessing.processors.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.mdb.clients.k8s.psmdb.Psmdb;
import com.onyxdb.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.mdb.clients.k8s.psmdb.PsmdbSpec;
import com.onyxdb.mdb.core.clusters.ClusterService;
import com.onyxdb.mdb.core.clusters.models.Cluster;
import com.onyxdb.mdb.taskProcessing.models.Task;
import com.onyxdb.mdb.taskProcessing.models.TaskProcessingResult;
import com.onyxdb.mdb.taskProcessing.models.TaskType;
import com.onyxdb.mdb.taskProcessing.models.payloads.ClusterTaskPayload;
import com.onyxdb.mdb.taskProcessing.processors.ClusterTaskProcessor;

import static com.onyxdb.mdb.core.clusters.ClusterMapper.DEFAULT_NAMESPACE;

public class MongoCreateClusterTaskProcessor extends ClusterTaskProcessor {
    private final PsmdbClient psmdbClient;

    public MongoCreateClusterTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient
    ) {
        super(objectMapper, clusterService);
        this.psmdbClient = psmdbClient;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGODB_CREATE_CLUSTER;
    }

    @Override
    protected TaskProcessingResult internalProcess(Task task, ClusterTaskPayload payload) {
        Cluster cluster = clusterService.getCluster(payload.clusterId());

        var psmdb = new Psmdb(
                DEFAULT_NAMESPACE,
                cluster.name(),
                PsmdbSpec.builder().build(cluster.name())
        );
        psmdbClient.createResource(psmdb);

        return TaskProcessingResult.success();
    }
}
