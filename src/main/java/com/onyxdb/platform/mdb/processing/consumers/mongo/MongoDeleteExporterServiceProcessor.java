package com.onyxdb.platform.mdb.processing.consumers.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbExporterServiceClient;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.processing.consumers.ClusterTaskProcessor;
import com.onyxdb.platform.mdb.processing.models.Task;
import com.onyxdb.platform.mdb.processing.models.TaskProcessingResult;
import com.onyxdb.platform.mdb.processing.models.TaskType;
import com.onyxdb.platform.mdb.processing.models.payloads.ClusterPayload;

import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_NAMESPACE;
import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_PROJECT;

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
        return TaskType.MONGO_DELETE_EXPORTER_SERVICE;
    }

    @Override
    protected TaskProcessingResult internalProcess(Task task, ClusterPayload payload) {
        Cluster cluster = clusterService.getClusterOrThrow(payload.clusterId());

        psmdbExporterServiceClient.deletePsmdbExporterService(
                DEFAULT_NAMESPACE,
                DEFAULT_PROJECT,
                cluster.name()
        );

        return TaskProcessingResult.success();
    }
}
