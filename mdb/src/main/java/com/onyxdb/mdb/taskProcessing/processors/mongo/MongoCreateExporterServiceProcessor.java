package com.onyxdb.mdb.taskProcessing.processors.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.mdb.clients.k8s.psmdb.PsmdbExporterService;
import com.onyxdb.mdb.clients.k8s.psmdb.PsmdbExporterServiceClient;
import com.onyxdb.mdb.core.clusters.ClusterService;
import com.onyxdb.mdb.core.clusters.models.Cluster;
import com.onyxdb.mdb.taskProcessing.models.Task;
import com.onyxdb.mdb.taskProcessing.models.TaskProcessingResult;
import com.onyxdb.mdb.taskProcessing.models.TaskType;
import com.onyxdb.mdb.taskProcessing.models.payloads.ClusterTaskPayload;
import com.onyxdb.mdb.taskProcessing.processors.ClusterTaskProcessor;

import static com.onyxdb.mdb.core.clusters.ClusterMapper.DEFAULT_NAMESPACE;

public class MongoCreateExporterServiceProcessor extends ClusterTaskProcessor {
    private final PsmdbExporterServiceClient psmdbExporterServiceClient;

    public MongoCreateExporterServiceProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbExporterServiceClient psmdbExporterServiceClient
    ) {
        super(objectMapper, clusterService);
        this.psmdbExporterServiceClient = psmdbExporterServiceClient;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGODB_CREATE_EXPORTER_SERVICE;
    }

    @Override
    protected TaskProcessingResult internalProcess(Task task, ClusterTaskPayload payload) {
        Cluster cluster = clusterService.getCluster(payload.clusterId());

        var psmdbExporterService = PsmdbExporterService.builder()
                .build(DEFAULT_NAMESPACE, cluster.name());
        psmdbExporterServiceClient.createResource(psmdbExporterService);

        return TaskProcessingResult.success();
    }
}
