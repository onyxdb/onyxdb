package com.onyxdb.platform.mdb.clusters;

import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.JSONB;
import org.jooq.Record;

import com.onyxdb.platform.generated.jooq.tables.records.ClustersRecord;
import com.onyxdb.platform.generated.openapi.models.ClusterBackupConfigDTO;
import com.onyxdb.platform.generated.openapi.models.ClusterResourcesDTO;
import com.onyxdb.platform.generated.openapi.models.ClusterStatusDTO;
import com.onyxdb.platform.generated.openapi.models.CreateMongoClusterRequestDTO;
import com.onyxdb.platform.generated.openapi.models.MongoClusterDTO;
import com.onyxdb.platform.generated.openapi.models.MongoConfigDTO;
import com.onyxdb.platform.generated.openapi.models.UpdateMongoClusterRequestDTO;
import com.onyxdb.platform.generated.openapi.models.UpdateMongoConfigDTO;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.clusters.models.ClusterBackupConfig;
import com.onyxdb.platform.mdb.clusters.models.ClusterConfig;
import com.onyxdb.platform.mdb.clusters.models.ClusterResources;
import com.onyxdb.platform.mdb.clusters.models.ClusterStatus;
import com.onyxdb.platform.mdb.clusters.models.ClusterType;
import com.onyxdb.platform.mdb.clusters.models.ClusterVersion;
import com.onyxdb.platform.mdb.clusters.models.CreateCluster;
import com.onyxdb.platform.mdb.clusters.models.UpdateCluster;
import com.onyxdb.platform.mdb.clusters.models.UpdateClusterConfig;
import com.onyxdb.platform.mdb.clusters.models.UpdateClusterResources;
import com.onyxdb.platform.mdb.exceptions.InternalServerErrorException;

/**
 * @author foxleren
 */
public class ClusterMapper {
    private final ObjectMapper objectMapper;

    public ClusterMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public CreateCluster createMongoClusterRequestDTOtoCreateCluster(
            CreateMongoClusterRequestDTO r,
            UUID createdBy
    ) {
        return new CreateCluster(
                r.getName(),
                r.getDescription(),
                r.getProjectId(),
                ClusterType.MONGODB,
                mongoConfigDTOtoClusterConfig(r.getConfig()),
                r.getDatabase().getName(),
                r.getUser().getName(),
                r.getUser().getPassword(),
                createdBy
        );
    }

    public ClusterConfig mongoConfigDTOtoClusterConfig(MongoConfigDTO c) {
        return new ClusterConfig(
                ClusterVersion.R.fromValue(ClusterVersion.typeAndVersionAsString(ClusterType.MONGODB, c.getVersion())),
                clusterResourcesDTOtoClusterResources(c.getResources()),
                c.getReplicas(),
                clusterBackupConfigDTOtoClusterBackupConfig(c.getBackup())
        );
    }

    public ClusterResources clusterResourcesDTOtoClusterResources(ClusterResourcesDTO rq) {
        return new ClusterResources(
                rq.getPresetId(),
                rq.getStorageClass(),
                rq.getStorage()
        );
    }

    public ClusterBackupConfig clusterBackupConfigDTOtoClusterBackupConfig(ClusterBackupConfigDTO c) {
        return new ClusterBackupConfig(
                c.getIsEnabled(),
                c.getSchedule(),
                c.getLimit()
        );
    }

    public Cluster createClusterToCluster(CreateCluster c, String namespace) {
        return Cluster.create(
                UUID.randomUUID(),
                c.name(),
                c.description(),
                c.projectId(),
                namespace,
                c.type(),
                c.config(),
                c.createdBy()
        );
    }

    public ClustersRecord clusterToClustersRecord(Cluster c) {
        try {
            return new ClustersRecord(
                    c.id(),
                    c.name(),
                    c.description(),
                    c.status().value(),
                    c.projectId(),
                    c.namespace(),
                    clusterTypeToJooqClusterType(c.type()),
                    JSONB.jsonb(objectMapper.writeValueAsString(c.config())),
                    c.createdAt(),
                    c.createdBy(),
                    c.isDeleted(),
                    c.deletedAt(),
                    c.deletedBy()
            );
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException(e);
        }
    }

    public com.onyxdb.platform.generated.jooq.enums.ClusterType clusterTypeToJooqClusterType(ClusterType t) {
        return com.onyxdb.platform.generated.jooq.enums.ClusterType.lookupLiteral(t.value());
    }

    public Cluster fromJooqRecord(Record r) {
        ClustersRecord rr = r.into(ClustersRecord.class);

        try {
            return new Cluster(
                    rr.getId(),
                    rr.getName(),
                    rr.getDescription(),
                    ClusterStatus.R.fromValue(rr.getStatus()),
                    rr.getProjectId(),
                    rr.getNamespace(),
                    ClusterType.R.fromValue(rr.getType().getLiteral()),
                    objectMapper.readValue((rr.getConfig()).data(), ClusterConfig.class),
                    rr.getCreatedAt(),
                    rr.getCreatedBy(),
                    rr.getIsDeleted(),
                    rr.getDeletedAt(),
                    rr.getDeletedBy()
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public MongoClusterDTO clusterToMongoClusterDTO(Cluster c) {
        return new MongoClusterDTO(
                c.id(),
                c.name(),
                c.description(),
                clusterStatusToClusterStatusDTO(c.status()),
                c.projectId(),
                c.namespace(),
                clusterConfigToMongoConfigDTO(c.config()),
                c.createdAt(),
                c.createdBy(),
                c.isDeleted(),
                c.deletedAt(),
                c.deletedBy()
        );
    }

    public ClusterStatusDTO clusterStatusToClusterStatusDTO(ClusterStatus s) {
        return new ClusterStatusDTO(
                s.value(),
                s.getDisplayValue()
        );
    }

    public MongoConfigDTO clusterConfigToMongoConfigDTO(ClusterConfig c) {
        return new MongoConfigDTO(
                c.version().getVersion(),
                clusterResourcesToClusterResourcesDTO(c.resources()),
                c.replicas(),
                clusterBackupConfigToClusterBackupConfigDTO(c.backup())
        );
    }

    public ClusterResourcesDTO clusterResourcesToClusterResourcesDTO(ClusterResources r) {
        return new ClusterResourcesDTO(
                r.presetId(),
                r.storageClass(),
                r.storage()
        );
    }

    public ClusterBackupConfigDTO clusterBackupConfigToClusterBackupConfigDTO(ClusterBackupConfig c) {
        return new ClusterBackupConfigDTO(
                c.isEnabled(),
                c.schedule(),
                c.limit()
        );
    }

    public UpdateCluster updateMongoClusterRequestDTOtoUpdateCluster(
            UUID clusterId,
            UpdateMongoClusterRequestDTO rq,
            UUID updatedBy
    ) {
        return new UpdateCluster(
                clusterId,
                rq.getDescription(),
                updateMongoClusterConfigDTOtoUpdateClusterConfig(rq.getConfig()),
                updatedBy
        );
    }

    public UpdateClusterConfig updateMongoClusterConfigDTOtoUpdateClusterConfig(UpdateMongoConfigDTO c) {
        return new UpdateClusterConfig(
                ClusterVersion.R.fromValue(ClusterVersion.typeAndVersionAsString(ClusterType.MONGODB, c.getVersion())),
                new UpdateClusterResources(c.getResources().getPresetId()),
                c.getReplicas(),
                clusterBackupConfigDTOtoClusterBackupConfig(c.getBackup())
        );
    }
}
