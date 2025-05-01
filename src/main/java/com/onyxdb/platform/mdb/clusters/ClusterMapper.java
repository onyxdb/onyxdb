package com.onyxdb.platform.mdb.clusters;

import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.JSONB;
import org.jooq.Record;

import com.onyxdb.platform.generated.jooq.tables.records.ClustersRecord;
import com.onyxdb.platform.generated.openapi.models.ClusterBackupConfigDTO;
import com.onyxdb.platform.generated.openapi.models.ClusterResourcesDTO;
import com.onyxdb.platform.generated.openapi.models.CreateMongoClusterRequestDTO;
import com.onyxdb.platform.generated.openapi.models.MongoConfigDTO;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.clusters.models.ClusterBackupConfig;
import com.onyxdb.platform.mdb.clusters.models.ClusterConfig;
import com.onyxdb.platform.mdb.clusters.models.ClusterResources;
import com.onyxdb.platform.mdb.clusters.models.ClusterType;
import com.onyxdb.platform.mdb.clusters.models.ClusterVersion;
import com.onyxdb.platform.mdb.clusters.models.CreateCluster;
import com.onyxdb.platform.mdb.exceptions.InternalServerErrorException;
import com.onyxdb.platform.mdb.utils.OnyxdbConsts;
import com.onyxdb.platform.mdb.utils.TimeUtils;

/**
 * @author foxleren
 */
public class ClusterMapper {
    public static final String DEFAULT_NAMESPACE = OnyxdbConsts.NAMESPACE;
    public static final String DEFAULT_PROJECT = OnyxdbConsts.PROJECT;

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

    public ClusterResources clusterResourcesDTOtoClusterResources(ClusterResourcesDTO r) {
        return new ClusterResources(
                r.getPresetId(),
                r.getStorageClass(),
                r.getStorage()
        );
    }

    public ClusterBackupConfig clusterBackupConfigDTOtoClusterBackupConfig(ClusterBackupConfigDTO c) {
        return new ClusterBackupConfig(
                c.getIsEnabled(),
                c.getSchedule()
        );
    }

    public Cluster createClusterToCluster(CreateCluster c, String namespace) {
        return new Cluster(
                UUID.randomUUID(),
                c.name(),
                c.description(),
                c.projectId(),
                namespace,
                c.type(),
                c.config(),
                TimeUtils.now(),
                c.createdBy(),
                false,
                null,
                null
        );
    }

    public ClustersRecord clusterToClustersRecord(Cluster c) {
        try {
            return new ClustersRecord(
                    c.id(),
                    c.name(),
                    c.description(),
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

//    public UpdateCluster v1MongoUpdateClusterRequestToUpdateCluster(
//            UUID clusterId,
//            V1MongoUpdateClusterRequest r
//    ) {
//        MongoConfigDTO v1MongoConfig = r.getConfig();
//        return new UpdateCluster(
//                clusterId,
//                r.getName(),
//                new ClusterConfig(
//                        ClusterResourcesDTOtoClusterResources(v1MongoConfig.getResources()),
//                        v1MongoConfig.getReplicas()
//                )
//        );
//    }
//    public com.onyxdb.platform.generated.jooq.enums.ClusterType clusterTypeToJooqClusterType(ClusterType t) {
//        return com.onyxdb.platform.generated.jooq.enums.ClusterType.lookupLiteral(t.value());
//    }

//    public com.onyxdb.mdb.generated.jooq.enums.ClusterVersion clusterVersionToJooqClusterVersion(ClusterVersion v) {
//        return com.onyxdb.mdb.generated.jooq.enums.ClusterVersion.lookupLiteral(v.value());
//    }

//    public MongodV8d0Config v1MongodConfigV8d0ToMongodV8d0Config(V1MongodConfigV8d0 c) {
//        return new MongodV8d0Config(
//                new MongodV8d0Net(
//                        c.getNet().getMaxIncomingConnections()
//                )
//        );
//    }

    public Cluster fromJooqRecord(Record r) {
        ClustersRecord rr = r.into(ClustersRecord.class);

        try {
            return new Cluster(
                    rr.getId(),
                    rr.getName(),
                    rr.getDescription(),
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

//    public V1MongoClusterResponse map(Cluster c) {
//        return new V1MongoClusterResponse(
//                c.id(),
//                c.name(),
//                c.description(),
//                new V1ClusterStatusResponse(
//                        "alive",
//                        "Все хосты работают нормально, все запущенные операции были успешно выполнены."
//                ),
//                c.projectId(),
//                map(c.config())
//        );
//    }

//    public MongoConfigDTO map(ClusterConfig c) {
//        return new MongoConfigDTO(
//                map(c.resources()),
//                c.replicas(),
//                new ClusterBackupConfigDTO()
//        );
//    }

    public ClusterResourcesDTO map(ClusterResources r) {
        return new ClusterResourcesDTO(
                r.presetId(),
                r.storageClass(),
                r.storage()
        );
    }

    public String toString(CreateCluster c) {
        try {
            return objectMapper.writeValueAsString(c);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
