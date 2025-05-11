package com.onyxdb.platform.mdb.operations.consumers.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.backups.BackupService;
import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.clusters.models.ClusterBackupConfig;
import com.onyxdb.platform.mdb.clusters.models.ClusterResources;
import com.onyxdb.platform.mdb.operations.consumers.ClusterTaskConsumer;
import com.onyxdb.platform.mdb.operations.models.Task;
import com.onyxdb.platform.mdb.operations.models.TaskResult;
import com.onyxdb.platform.mdb.operations.models.TaskType;
import com.onyxdb.platform.mdb.operations.models.payload.ClusterPayload;
import com.onyxdb.platform.mdb.projects.Project;
import com.onyxdb.platform.mdb.projects.ProjectRepository;
import com.onyxdb.platform.mdb.resourcePresets.ResourcePreset;
import com.onyxdb.platform.mdb.resourcePresets.ResourcePresetService;

@Component
public class MongoApplyPsmdbTaskConsumer extends ClusterTaskConsumer {
    private final PsmdbClient psmdbClient;
    private final ResourcePresetService resourcePresetService;
    private final BackupService backupService;
    private final ProjectRepository projectRepository;

    public MongoApplyPsmdbTaskConsumer(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient,
            ResourcePresetService resourcePresetService,
            BackupService backupService,
            ProjectRepository projectRepository
    ) {
        super(objectMapper, clusterService);
        this.psmdbClient = psmdbClient;
        this.resourcePresetService = resourcePresetService;
        this.backupService = backupService;
        this.projectRepository = projectRepository;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_APPLY_PSMDB;
    }

    @Override
    protected TaskResult internalProcess(Task task, ClusterPayload payload) {
        Cluster cluster = clusterService.getClusterOrThrow(payload.clusterId());
        Project project = projectRepository.getProjectOrThrow(cluster.projectId());

        ClusterResources resources = cluster.config().resources();
        ClusterBackupConfig backupConfig = cluster.config().backup();
        ResourcePreset resourcePreset = resourcePresetService.getOrThrow(cluster.config().resources().presetId());

        psmdbClient.applyPsmdbCr(
                cluster.namespace(),
                project.name(),
                cluster.name(),
                cluster.config().replicas(),
                resourcePreset.vcpu(),
                resourcePreset.ram(),
                resources.storageClass(),
                resources.storage(),
                backupConfig.isEnabled(),
                backupConfig.schedule(),
                backupConfig.limit(),
                backupService.getMinioUrl(),
                backupService.getMinioSecret(),
                backupService.getMinioBucket()
        );

        return TaskResult.success();
    }
}
