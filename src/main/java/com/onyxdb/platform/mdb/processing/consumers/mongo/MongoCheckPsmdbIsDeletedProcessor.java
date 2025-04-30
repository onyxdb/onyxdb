package com.onyxdb.platform.mdb.processing.consumers.mongo;

import java.time.Duration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.mdb.clusters.ClusterRepository;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.models.Cluster;
import com.onyxdb.platform.mdb.processing.consumers.ClusterTaskProcessor;
import com.onyxdb.platform.mdb.processing.models.Task;
import com.onyxdb.platform.mdb.processing.models.TaskProcessingResult;
import com.onyxdb.platform.mdb.processing.models.TaskType;
import com.onyxdb.platform.mdb.processing.models.payloads.ClusterPayload;

import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_NAMESPACE;
import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_PROJECT;

@Component
public class MongoCheckPsmdbIsDeletedProcessor extends ClusterTaskProcessor {
    private final PsmdbClient psmdbClient;
    private final ClusterRepository clusterRepository;

    public MongoCheckPsmdbIsDeletedProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient,
            ClusterRepository clusterRepository
    ) {
        super(objectMapper, clusterService);
        this.psmdbClient = psmdbClient;
        this.clusterRepository = clusterRepository;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_CHECK_PSMDB_IS_DELETED;
    }

    @Override
    protected TaskProcessingResult internalProcess(Task task, ClusterPayload payload) {
        Cluster cluster = clusterService.getCluster(payload.clusterId());

        boolean psmdbExists = psmdbClient.psmdbExists(DEFAULT_NAMESPACE, DEFAULT_PROJECT, cluster.name());
        if (psmdbExists) {
            return TaskProcessingResult.scheduled(
                    task.getScheduledAtWithDelay(Duration.ofSeconds(30))
            );
        }
        clusterRepository.markClusterDeleted(cluster.id());

        return TaskProcessingResult.success();
    }
}
