package com.onyxdb.mdb.core.clusters;

import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.Record;

import com.onyxdb.mdb.core.clusters.models.Cluster;
import com.onyxdb.mdb.core.clusters.models.ClusterConfig;
import com.onyxdb.mdb.core.clusters.models.ClusterResources;
import com.onyxdb.mdb.core.clusters.models.ClusterType;
import com.onyxdb.mdb.core.clusters.models.CreateCluster;
import com.onyxdb.mdb.core.clusters.models.UpdateCluster;
import com.onyxdb.mdb.generated.jooq.tables.records.ClustersRecord;
import com.onyxdb.mdb.generated.openapi.models.V1ClusterResources;
import com.onyxdb.mdb.generated.openapi.models.V1ClusterStatusResponse;
import com.onyxdb.mdb.generated.openapi.models.V1CreateMongoClusterRequest;
import com.onyxdb.mdb.generated.openapi.models.V1MongoClusterResponse;
import com.onyxdb.mdb.generated.openapi.models.V1MongoConfig;
import com.onyxdb.mdb.generated.openapi.models.V1MongoUpdateClusterRequest;

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

    public CreateCluster v1CreateMongoClusterRequestToCreateCluster(V1CreateMongoClusterRequest r) {
        V1MongoConfig v1MongoConfig = r.getConfig();
        return new CreateCluster(
                r.getName(),
                r.getDescription(),
                r.getProjectId(),
                DEFAULT_NAMESPACE,
                ClusterType.MONGODB,
                new ClusterConfig(
                        v1ClusterResourcesToClusterResources(v1MongoConfig.getResources()),
                        v1MongoConfig.getReplicas()
                )
//                ClusterVersion.fromStringVersionWithDots(ClusterType.MONGODB, v1MongoConfig.getVersion()),
//                ClusterConfig.builder()
//                        .withMongoV8d0(v1MongoConfigV8d0ToMongoV8d0Config(v1MongoConfig.getMongodbV8d0()))
//                        .build()
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

    public Cluster createClusterToCluster(CreateCluster c) {
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

    public com.onyxdb.mdb.generated.jooq.enums.ClusterType clusterTypeToJooqClusterType(ClusterType t) {
        return com.onyxdb.mdb.generated.jooq.enums.ClusterType.lookupLiteral(t.value());
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
}
