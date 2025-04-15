package com.onyxdb.mdb.taskProcessing.processors.mongo;

import java.time.Duration;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.mdb.core.clusters.ClusterService;
import com.onyxdb.mdb.core.clusters.HostService;
import com.onyxdb.mdb.core.clusters.models.Cluster;
import com.onyxdb.mdb.core.clusters.models.Host;
import com.onyxdb.mdb.taskProcessing.models.Task;
import com.onyxdb.mdb.taskProcessing.models.TaskProcessingResult;
import com.onyxdb.mdb.taskProcessing.models.TaskType;
import com.onyxdb.mdb.taskProcessing.models.payloads.ClusterTaskPayload;
import com.onyxdb.mdb.taskProcessing.processors.ClusterTaskProcessor;

import static com.onyxdb.mdb.core.clusters.ClusterMapper.DEFAULT_NAMESPACE;
import static com.onyxdb.mdb.core.clusters.ClusterMapper.DEFAULT_PROJECT;

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
        return TaskType.MONGODB_CHECK_PSMDB_READINESS;
    }

    @Override
    protected TaskProcessingResult internalProcess(Task task, ClusterTaskPayload payload) {
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

        hostService.createHosts(hosts);

        return TaskProcessingResult.success();
    }
}
