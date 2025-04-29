package com.onyxdb.platform.mdb.clusters;

import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.Record;

import com.onyxdb.platform.core.clusters.models.Cluster;
import com.onyxdb.platform.core.clusters.models.ClusterConfig;
import com.onyxdb.platform.core.clusters.models.ClusterResources;
import com.onyxdb.platform.core.clusters.models.ClusterType;
import com.onyxdb.platform.core.clusters.models.ClusterToCreate;
import com.onyxdb.platform.core.clusters.models.UpdateCluster;
import com.onyxdb.platform.generated.jooq.tables.records.ClustersRecord;
import com.onyxdb.platform.generated.openapi.models.V1ClusterResources;
import com.onyxdb.platform.generated.openapi.models.V1ClusterStatusResponse;
import com.onyxdb.platform.generated.openapi.models.V1CreateMongoClusterRequest;
import com.onyxdb.platform.generated.openapi.models.V1MongoClusterResponse;
import com.onyxdb.platform.generated.openapi.models.V1MongoConfig;
import com.onyxdb.platform.generated.openapi.models.V1MongoUpdateClusterRequest;

/**
 * @author foxleren
 */
public class ClusterMapper {
    public static final String DEFAULT_NAMESPACE = "onyxdb";
    public static final String DEFAULT_PROJECT = "sandbox";

    private final ObjectMapper objectMapper;

    public ClusterMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ClusterToCreate v1CreateMongoClusterRequestToCreateCluster(V1CreateMongoClusterRequest r) {
        V1MongoConfig v1MongoConfig = r.getConfig();
        return new ClusterToCreate(
                r.getName(),
                r.getDescription(),
                r.getProjectId(),
                DEFAULT_NAMESPACE,
                ClusterType.MONGODB,
                mapToClusterConfig(v1MongoConfig),
                r.getDatabase().getName(),
                r.getUser().getName(),
                r.getUser().getPassword()
//                ClusterVersion.fromStringVersionWithDots(ClusterType.MONGODB, v1MongoConfig.getVersion()),
//                ClusterConfig.builder()
//                        .withMongoV8d0(v1MongoConfigV8d0ToMongoV8d0Config(v1MongoConfig.getMongodbV8d0()))
//                        .build()
        );
    }

    public ClusterConfig mapToClusterConfig(V1MongoConfig c) {
        return new ClusterConfig(
                v1ClusterResourcesToClusterResources(c.getResources()),
                c.getReplicas()
        );
    }

    public UpdateCluster v1MongoUpdateClusterRequestToUpdateCluster(
            UUID clusterId,
            V1MongoUpdateClusterRequest r
    ) {
        V1MongoConfig v1MongoConfig = r.getConfig();
        return new UpdateCluster(
                clusterId,
                r.getName(),
                new ClusterConfig(
                        v1ClusterResourcesToClusterResources(v1MongoConfig.getResources()),
                        v1MongoConfig.getReplicas()
                )
        );
    }

//    public MongoV8d0Config v1MongoConfigV8d0ToMongoV8d0Config(V1MongoConfigV8d0 c) {
//        return new MongoV8d0Config(
//                v1MongodV8d0ToMongodV8d0(c.getMongod())
//        );
//    }

//    public MongodV8d0 v1MongodV8d0ToMongodV8d0(V1MongodV8d0 m) {
//        return new MongodV8d0(
//                v1ClusterResourcesToClusterResources(m.getResources()),
//                v1MongodConfigV8d0ToMongodV8d0Config(m.getConfig())
//        );
//    }

    public ClusterResources v1ClusterResourcesToClusterResources(V1ClusterResources r) {
        return new ClusterResources(
                r.getPresetId(),
                r.getStorageClass(),
                r.getStorage()
        );
    }

    public Cluster createClusterToCluster(ClusterToCreate c) {
        return new Cluster(
                UUID.randomUUID(),
                c.name(),
                c.description(),
                c.projectId(),
                c.type(),
//                c.version(),
                c.config()
        );
    }

    public com.onyxdb.platform.generated.jooq.enums.ClusterType clusterTypeToJooqClusterType(ClusterType t) {
        return com.onyxdb.platform.generated.jooq.enums.ClusterType.lookupLiteral(t.value());
    }

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
                    ClusterType.fromValue(rr.getType().getLiteral()),
                    objectMapper.readValue((rr.getConfig()).data(), ClusterConfig.class)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public V1MongoClusterResponse map(Cluster c) {
        return new V1MongoClusterResponse(
                c.id(),
                c.name(),
                c.description(),
                new V1ClusterStatusResponse(
                        "alive",
                        "Все хосты работают нормально, все запущенные операции были успешно выполнены."
                ),
                c.projectId(),
                map(c.config())
        );
    }

    public V1MongoConfig map(ClusterConfig c) {
        return new V1MongoConfig(
                map(c.resources()),
                c.replicas()
        );
    }

    public V1ClusterResources map(ClusterResources r) {
        return new V1ClusterResources(
                r.presetId(),
                r.storageClass(),
                r.storage()
        );
    }

    public String toString(ClusterToCreate c) {
        try {
            return objectMapper.writeValueAsString(c);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
