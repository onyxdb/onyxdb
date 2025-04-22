package com.onyxdb.platform.taskProcessing.processors.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.core.clusters.ClusterService;
import com.onyxdb.platform.core.clusters.models.Cluster;
import com.onyxdb.platform.core.resourcePresets.ResourcePreset;
import com.onyxdb.platform.core.resourcePresets.ResourcePresetService;
import com.onyxdb.platform.taskProcessing.models.Task;
import com.onyxdb.platform.taskProcessing.models.TaskProcessingResult;
import com.onyxdb.platform.taskProcessing.models.TaskType;
import com.onyxdb.platform.taskProcessing.models.payloads.ClusterTaskPayload;
import com.onyxdb.platform.taskProcessing.processors.ClusterTaskProcessor;

import static com.onyxdb.platform.core.clusters.ClusterMapper.DEFAULT_NAMESPACE;
import static com.onyxdb.platform.core.clusters.ClusterMapper.DEFAULT_PROJECT;

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
