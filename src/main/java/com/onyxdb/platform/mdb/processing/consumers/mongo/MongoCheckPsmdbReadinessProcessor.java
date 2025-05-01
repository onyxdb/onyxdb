package com.onyxdb.platform.mdb.processing.consumers.mongo;

import java.time.Duration;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.hosts.HostService;
import com.onyxdb.platform.mdb.models.Host;
import com.onyxdb.platform.mdb.processing.consumers.ClusterTaskProcessor;
import com.onyxdb.platform.mdb.processing.models.Task;
import com.onyxdb.platform.mdb.processing.models.TaskProcessingResult;
import com.onyxdb.platform.mdb.processing.models.TaskType;
import com.onyxdb.platform.mdb.processing.models.payloads.ClusterPayload;

import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_NAMESPACE;
import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_PROJECT;

public class MongoCheckPsmdbReadinessProcessor extends ClusterTaskProcessor {
    private final PsmdbClient psmdbClient;
    private final HostService hostService;

    public MongoCheckPsmdbReadinessProcessor(
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
    protected TaskProcessingResult internalProcess(Task task, ClusterPayload payload) {
        Cluster cluster = clusterService.getCluster(payload.clusterId());

        boolean isReady = psmdbClient.isResourceReady(DEFAULT_NAMESPACE, DEFAULT_PROJECT, cluster.name());
        if (!isReady) {
            return TaskProcessingResult.scheduled(
                    task.getScheduledAtWithDelay(Duration.ofSeconds(30))
            );
        }

        List<Host> hosts = psmdbClient.getPsmdbPods(DEFAULT_NAMESPACE, DEFAULT_PROJECT, cluster.name())
                .stream()
                .map(podName -> new Host(podName, cluster.id()))
                .toList();

        hostService.upsertHosts(hosts);

        return TaskProcessingResult.success();
    }
}
