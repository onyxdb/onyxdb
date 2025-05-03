package com.onyxdb.platform.mdb.operations.consumers.mongo;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.hosts.HostRepository;
import com.onyxdb.platform.mdb.models.Host;
import com.onyxdb.platform.mdb.operations.consumers.ClusterTaskConsumer;
import com.onyxdb.platform.mdb.operations.models.Task;
import com.onyxdb.platform.mdb.operations.models.TaskResult;
import com.onyxdb.platform.mdb.operations.models.TaskType;
import com.onyxdb.platform.mdb.operations.models.payload.ClusterPayload;

import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_PROJECT;

@Component
public class MongoUpdateHostsTaskConsumer extends ClusterTaskConsumer {
    private final PsmdbClient psmdbClient;
    private final HostRepository hostRepository;

    public MongoUpdateHostsTaskConsumer(
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
    protected TaskResult internalProcess(Task task, ClusterPayload payload) {
        Cluster cluster = clusterService.getClusterOrThrow(payload.clusterId());

        List<String> hostnames = PsmdbClient.calculatePsmdbHostNames(
                DEFAULT_PROJECT,
                cluster.name(),
                cluster.config().replicas()
        );
        System.err.println(hostnames);
        List<Host> hosts = hostnames.stream().map(name -> new Host(name, cluster.id())).toList();
        hostRepository.upsertHosts(hosts);
        hostRepository.deleteNotMatchingHosts(cluster.id(), hostnames);

        return TaskResult.success();
    }
}
