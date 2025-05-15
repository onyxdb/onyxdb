package com.onyxdb.platform.mdb.operations.consumers.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.operations.consumers.ClusterTaskConsumer;
import com.onyxdb.platform.mdb.operations.models.Task;
import com.onyxdb.platform.mdb.operations.models.TaskResult;
import com.onyxdb.platform.mdb.operations.models.TaskType;
import com.onyxdb.platform.mdb.operations.models.payload.ClusterPayload;
import com.onyxdb.platform.mdb.quotas.QuotaService;

@Component
public class MongoUpdateQuotaAfterClusterDeletionTaskConsumer extends ClusterTaskConsumer {
    private final QuotaService quotaService;

    public MongoUpdateQuotaAfterClusterDeletionTaskConsumer(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            QuotaService quotaService
    ) {
        super(objectMapper, clusterService);
        this.quotaService = quotaService;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_UPDATE_QUOTA_AFTER_CLUSTER_DELETION;
    }

    @Override
    protected TaskResult internalProcess(Task task, ClusterPayload payload) {
        Cluster cluster = clusterService.getClusterOrThrow(payload.clusterId());

        // TODO uncomment!!!
//        quotaService.applyQuotaByClusterConfig(cluster.projectId(), null, cluster.config());

        return TaskResult.success();
    }
}
