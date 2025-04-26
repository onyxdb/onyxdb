package com.onyxdb.platform.processing.consumers.mongo;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.core.clusters.models.Cluster;
import com.onyxdb.platform.core.clusters.models.Host;
import com.onyxdb.platform.core.resourcePresets.ResourcePreset;
import com.onyxdb.platform.core.resourcePresets.ResourcePresetService;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.hosts.HostRepository;
import com.onyxdb.platform.mdb.hosts.HostService;
import com.onyxdb.platform.processing.consumers.ClusterTaskProcessor;
import com.onyxdb.platform.processing.models.Task;
import com.onyxdb.platform.processing.models.TaskProcessingResult;
import com.onyxdb.platform.processing.models.TaskType;
import com.onyxdb.platform.processing.models.payloads.ClusterPayload;

import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_NAMESPACE;
import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_PROJECT;

@Component
public class MongoUpdateHostsTaskProcessor extends ClusterTaskProcessor {
    private final PsmdbClient psmdbClient;
    private final HostRepository hostRepository;

    public MongoUpdateHostsTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient,
            HostRepository hostRepository
    ) {
        super(objectMapper, clusterService);
        this.psmdbClient = psmdbClient;
        this.hostRepository = hostRepository;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_UPDATE_HOSTS;
    }

    @Override
    protected TaskProcessingResult internalProcess(Task task, ClusterPayload payload) {
        Cluster cluster = clusterService.getCluster(payload.clusterId());

        List<String> hostnames = psmdbClient.calculatePsmdbHostnames(
                DEFAULT_PROJECT,
                cluster.name(),
                cluster.config().replicas()
        );
        System.err.println(hostnames);
        List<Host> hosts = hostnames.stream().map(name -> new Host(name, cluster.id())).toList();
        hostRepository.upsertHosts(hosts);
        hostRepository.deleteNotMatchingHosts(cluster.id(), hostnames);

        return TaskProcessingResult.success();
    }
}
