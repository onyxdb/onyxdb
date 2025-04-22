package com.onyxdb.platform.taskProcessing.processors.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.clients.k8s.psmdb.PsmdbExporterService;
import com.onyxdb.platform.clients.k8s.psmdb.PsmdbExporterServiceClient;
import com.onyxdb.platform.core.clusters.ClusterService;
import com.onyxdb.platform.core.clusters.models.Cluster;
import com.onyxdb.platform.taskProcessing.models.Task;
import com.onyxdb.platform.taskProcessing.models.TaskProcessingResult;
import com.onyxdb.platform.taskProcessing.models.TaskType;
import com.onyxdb.platform.taskProcessing.models.payloads.ClusterTaskPayload;
import com.onyxdb.platform.taskProcessing.processors.ClusterTaskProcessor;

import static com.onyxdb.platform.core.clusters.ClusterMapper.DEFAULT_NAMESPACE;

public class MongoDeleteExporterServiceProcessor extends ClusterTaskProcessor {
    private final PsmdbExporterServiceClient psmdbExporterServiceClient;

    public MongoDeleteExporterServiceProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbExporterServiceClient psmdbExporterServiceClient
    ) {
        super(objectMapper, clusterService);
        this.psmdbExporterServiceClient = psmdbExporterServiceClient;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGODB_DELETE_EXPORTER_SERVICE;
    }

    @Override
    protected TaskProcessingResult internalProcess(Task task, ClusterTaskPayload payload) {
        Cluster cluster = clusterService.getCluster(payload.clusterId());

        var psmdbExporterService = PsmdbExporterService.builder()
                .build(DEFAULT_NAMESPACE, cluster.name());
        psmdbExporterServiceClient.deleteResource(psmdbExporterService);

        return TaskProcessingResult.success();
    }
}
