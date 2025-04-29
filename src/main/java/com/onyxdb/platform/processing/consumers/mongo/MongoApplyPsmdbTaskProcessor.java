package com.onyxdb.platform.processing.consumers.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.core.clusters.models.Cluster;
import com.onyxdb.platform.core.resourcePresets.ResourcePreset;
import com.onyxdb.platform.core.resourcePresets.ResourcePresetService;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.processing.consumers.ClusterTaskProcessor;
import com.onyxdb.platform.processing.models.Task;
import com.onyxdb.platform.processing.models.TaskProcessingResult;
import com.onyxdb.platform.processing.models.TaskType;
import com.onyxdb.platform.processing.models.payloads.ClusterPayload;

import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_NAMESPACE;
import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_PROJECT;

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
        return TaskType.MONGO_APPLY_PSMDB;
    }

    @Override
    protected TaskProcessingResult internalProcess(Task task, ClusterPayload payload) {
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
//        throw new RuntimeException();

        return TaskProcessingResult.success();
    }
}
