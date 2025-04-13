package com.onyxdb.mdb.taskProcessing.processors.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.mdb.core.clusters.ClusterService;
import com.onyxdb.mdb.core.clusters.models.Cluster;
import com.onyxdb.mdb.core.resourcePresets.ResourcePreset;
import com.onyxdb.mdb.core.resourcePresets.ResourcePresetService;
import com.onyxdb.mdb.taskProcessing.models.Task;
import com.onyxdb.mdb.taskProcessing.models.TaskProcessingResult;
import com.onyxdb.mdb.taskProcessing.models.TaskType;
import com.onyxdb.mdb.taskProcessing.models.payloads.ClusterTaskPayload;
import com.onyxdb.mdb.taskProcessing.processors.ClusterTaskProcessor;

import static com.onyxdb.mdb.core.clusters.ClusterMapper.DEFAULT_NAMESPACE;
import static com.onyxdb.mdb.core.clusters.ClusterMapper.DEFAULT_PROJECT;

public class MongoApplyPsmdbTaskProcessor extends ClusterTaskProcessor {
    private final PsmdbClient psmdbClient;
    private final ResourcePresetService resourcePresetService;

    public MongoApplyPsmdbTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient,
            ResourcePresetService resourcePresetService
    ) {
        super(objectMapper, clusterService);
        this.psmdbClient = psmdbClient;
        this.resourcePresetService = resourcePresetService;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGODB_APPLY_PSMDB;
    }

    @Override
    protected TaskProcessingResult internalProcess(Task task, ClusterTaskPayload payload) {
        Cluster cluster = clusterService.getCluster(payload.clusterId());

        ResourcePreset resourcePreset = resourcePresetService.getOrThrow(cluster.config().resources().presetId());

        psmdbClient.applyPsmdbCr(
                DEFAULT_NAMESPACE,
                DEFAULT_PROJECT,
                cluster.name(),
                cluster.config().replicas(),
                resourcePreset.vcpu(),
                resourcePreset.ram()
        );

        return TaskProcessingResult.success();
    }
}
