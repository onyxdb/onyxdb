package com.onyxdb.platform.mdb.backups;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.mdb.clusters.ClusterRepository;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.exceptions.BadRequestException;
import com.onyxdb.platform.mdb.operations.models.Operation;
import com.onyxdb.platform.mdb.operations.models.OperationType;
import com.onyxdb.platform.mdb.operations.models.payload.MongoCreateBackupPayload;
import com.onyxdb.platform.mdb.operations.models.payload.MongoDeleteBackupPayload;
import com.onyxdb.platform.mdb.operations.repositories.OperationRepository;
import com.onyxdb.platform.mdb.projects.Project;
import com.onyxdb.platform.mdb.projects.ProjectRepository;
import com.onyxdb.platform.mdb.utils.ObjectMapperUtils;
import com.onyxdb.platform.mdb.utils.TimeUtils;

@Service
public class BackupService {
    private final String minioUrl;
    private final String minioSecret;
    private final String minioBucket;

    private final PsmdbClient psmdbClient;
    private final ClusterRepository clusterRepository;
    private final ProjectRepository projectRepository;
    private final ObjectMapper objectMapper;
    private final OperationRepository operationRepository;

    public BackupService(
            @Value("${onyxdb.minio.url}")
            String minioUrl,
            @Value("${onyxdb.minio.secret}")
            String minioSecret,
            @Value("${onyxdb.minio.bucket}")
            String minioBucket,
            PsmdbClient psmdbClient,
            ClusterRepository clusterRepository,
            ProjectRepository projectRepository,
            ObjectMapper objectMapper,
            OperationRepository operationRepository
    ) {
        this.minioUrl = minioUrl;
        this.minioSecret = minioSecret;
        this.minioBucket = minioBucket;
        this.psmdbClient = psmdbClient;
        this.clusterRepository = clusterRepository;
        this.projectRepository = projectRepository;
        this.objectMapper = objectMapper;
        this.operationRepository = operationRepository;
    }

    public String getMinioUrl() {
        return minioUrl;
    }

    public String getMinioSecret() {
        return minioSecret;
    }

    public String getMinioBucket() {
        return minioBucket;
    }

    public List<Backup> listBackups(UUID clusterId) {
        Cluster cluster = clusterRepository.getClusterOrThrow(clusterId);
        Project project = projectRepository.getProjectOrThrow(cluster.projectId(), false);

        return psmdbClient.listBackups(
                cluster.namespace(),
                project.name(),
                cluster.name()
        );
    }

    public UUID createBackup(UUID clusterId, UUID createdBy) {
        clusterRepository.getClusterOrThrow(clusterId);
        var operation = Operation.scheduledWithPayload(
                OperationType.MONGO_CREATE_BACKUP,
                clusterId,
                createdBy,
                ObjectMapperUtils.convertToString(objectMapper, new MongoCreateBackupPayload(clusterId, TimeUtils.now()))
        );

        operationRepository.createOperation(operation);

        return operation.id();
    }

    public UUID deleteBackup(UUID clusterId, String backupName, UUID deletedBy) {
        Cluster cluster = clusterRepository.getClusterOrThrow(clusterId);
        Project project = projectRepository.getProjectOrThrow(cluster.projectId(), false);

        Set<String> existingBackupNames = psmdbClient.listBackups(
                cluster.namespace(),
                project.name(),
                cluster.name()
        ).stream().map(Backup::name).collect(Collectors.toSet());

        if (!existingBackupNames.contains(backupName)) {
            throw new BadRequestException("Backup with name '%s' is not found".formatted(backupName));
        }

        var operation = Operation.scheduledWithPayload(
                OperationType.MONGO_DELETE_BACKUP,
                clusterId,
                deletedBy,
                ObjectMapperUtils.convertToString(objectMapper, new MongoDeleteBackupPayload(clusterId, backupName))
        );

        operationRepository.createOperation(operation);

        return operation.id();
    }
}
