package com.onyxdb.platform.mdb.operations.consumers.mongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.operations.consumers.TaskConsumer;
import com.onyxdb.platform.mdb.operations.models.Task;
import com.onyxdb.platform.mdb.operations.models.TaskResult;
import com.onyxdb.platform.mdb.operations.models.TaskType;
import com.onyxdb.platform.mdb.operations.models.payload.MongoCreateBackupPayload;
import com.onyxdb.platform.mdb.projects.Project;
import com.onyxdb.platform.mdb.projects.ProjectRepository;

@Component
public class MongoCheckBackupIsReadyTaskConsumer extends TaskConsumer<MongoCreateBackupPayload> {
    private final PsmdbClient psmdbClient;
    private final ProjectRepository projectRepository;

    public MongoCheckBackupIsReadyTaskConsumer(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient,
            ProjectRepository projectRepository
    ) {
        super(objectMapper, clusterService);
        this.psmdbClient = psmdbClient;
        this.projectRepository = projectRepository;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_CHECK_BACKUP_IS_READY;
    }

    @Override
    protected TaskResult internalProcess(Task task, MongoCreateBackupPayload payload) {
        Cluster cluster = clusterService.getClusterOrThrow(payload.clusterId());
        Project project = projectRepository.getProjectOrThrow(cluster.projectId(), false);

        boolean isReady = psmdbClient.isPsmdbBackupReady(
                cluster.namespace(),
                project.name(),
                cluster.name(),
                payload.createdAt()
        );

        if (!isReady) {
            return TaskResult.error();
        }

        return TaskResult.success();
    }

    @Override
    protected MongoCreateBackupPayload parsePayload(Task task) throws JsonProcessingException {
        return objectMapper.readValue(task.payload(), MongoCreateBackupPayload.class);
    }
}
