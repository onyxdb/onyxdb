package com.onyxdb.platform.mdb.operations.consumers.mongo;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.hosts.HostService;
import com.onyxdb.platform.mdb.models.Host;
import com.onyxdb.platform.mdb.operations.consumers.ClusterTaskConsumer;
import com.onyxdb.platform.mdb.operations.models.Task;
import com.onyxdb.platform.mdb.operations.models.TaskResult;
import com.onyxdb.platform.mdb.operations.models.TaskType;
import com.onyxdb.platform.mdb.operations.models.payload.ClusterPayload;

import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_NAMESPACE;
import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_PROJECT;

public class MongoCheckPsmdbReadinessConsumer extends ClusterTaskConsumer {
    private final PsmdbClient psmdbClient;
    private final HostService hostService;

    public MongoCheckPsmdbReadinessConsumer(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient,
            HostService hostService
    ) {
        super(objectMapper, clusterService);
        this.psmdbClient = psmdbClient;
        this.hostService = hostService;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_CHECK_PSMDB_READINESS;
    }

    @Override
    protected TaskResult internalProcess(Task task, ClusterPayload payload) {
        Cluster cluster = clusterService.getClusterOrThrow(payload.clusterId());

        boolean isReady = psmdbClient.isResourceReady(DEFAULT_NAMESPACE, DEFAULT_PROJECT, cluster.name());
        if (!isReady) {
            return TaskResult.error();
        }

        List<Host> hosts = psmdbClient.getPsmdbPods(DEFAULT_NAMESPACE, DEFAULT_PROJECT, cluster.name())
                .stream()
                .map(podName -> new Host(podName, cluster.id()))
                .toList();

        hostService.upsertHosts(hosts);

//        throw new RuntimeException("FIX");

        return TaskResult.success();
    }
}
