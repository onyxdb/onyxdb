package com.onyxdb.platform.mdb.operations.consumers.mongo;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.clusters.models.Host;
import com.onyxdb.platform.mdb.hosts.HostRepository;
import com.onyxdb.platform.mdb.operations.consumers.ClusterTaskConsumer;
import com.onyxdb.platform.mdb.operations.models.Task;
import com.onyxdb.platform.mdb.operations.models.TaskResult;
import com.onyxdb.platform.mdb.operations.models.TaskType;
import com.onyxdb.platform.mdb.operations.models.payload.ClusterPayload;
import com.onyxdb.platform.mdb.projects.Project;
import com.onyxdb.platform.mdb.projects.ProjectRepository;

@Component
public class MongoUpdateHostsTaskConsumer extends ClusterTaskConsumer {
    private final HostRepository hostRepository;
    private final TransactionTemplate transactionTemplate;
    private final ProjectRepository projectRepository;

    public MongoUpdateHostsTaskConsumer(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            HostRepository hostRepository,
            TransactionTemplate transactionTemplate,
            ProjectRepository projectRepository
    ) {
        super(objectMapper, clusterService);
        this.hostRepository = hostRepository;
        this.transactionTemplate = transactionTemplate;
        this.projectRepository = projectRepository;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_UPDATE_HOSTS;
    }

    @Override
    protected TaskResult internalProcess(Task task, ClusterPayload payload) {
        Cluster cluster = clusterService.getClusterOrThrow(payload.clusterId());
        Project project = projectRepository.getProjectOrThrow(cluster.projectId());

        List<String> hostnames = PsmdbClient.calculatePsmdbHostNames(
                project.name(),
                cluster.name(),
                cluster.config().replicas()
        );
        List<Host> hosts = hostnames.stream().map(name -> new Host(name, cluster.id())).toList();

        transactionTemplate.executeWithoutResult(status -> {
            hostRepository.upsertHosts(hosts);
            hostRepository.deleteNotMatchingHosts(cluster.id(), hostnames);
        });

        return TaskResult.success();
    }
}
