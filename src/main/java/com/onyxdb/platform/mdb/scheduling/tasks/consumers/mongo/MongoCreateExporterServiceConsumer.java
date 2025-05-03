package com.onyxdb.platform.mdb.scheduling.tasks.consumers.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbExporterServiceClient;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.scheduling.operations.models.payloads.ClusterPayload;
import com.onyxdb.platform.mdb.scheduling.tasks.consumers.ClusterTaskConsumer;
import com.onyxdb.platform.mdb.scheduling.tasks.models.Task;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskResult;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskType;

import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_NAMESPACE;
import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_PROJECT;

public class MongoCreateExporterServiceConsumer extends ClusterTaskConsumer {
    private final PsmdbExporterServiceClient psmdbExporterServiceClient;

    public MongoCreateExporterServiceConsumer(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbExporterServiceClient psmdbExporterServiceClient
    ) {
        super(objectMapper, clusterService);
        this.psmdbExporterServiceClient = psmdbExporterServiceClient;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_APPLY_EXPORTER_SERVICE;
    }

    @Override
    protected TaskResult internalProcess(Task task, ClusterPayload payload) {
        Cluster cluster = clusterService.getClusterOrThrow(payload.clusterId());

        psmdbExporterServiceClient.applyPsmdbExporterService(
                DEFAULT_NAMESPACE,
                DEFAULT_PROJECT,
                cluster.name()
        );

        return TaskResult.success();
    }
}
