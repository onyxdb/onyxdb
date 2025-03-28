package com.onyxdb.mdb.core.clusters;

import java.util.UUID;

import com.onyxdb.mdb.core.clusters.models.Cluster;
import com.onyxdb.mdb.core.clusters.models.ClusterConfig;
import com.onyxdb.mdb.core.clusters.models.ClusterResources;
import com.onyxdb.mdb.core.clusters.models.ClusterType;
import com.onyxdb.mdb.core.clusters.models.ClusterVersion;
import com.onyxdb.mdb.core.clusters.models.CreateCluster;
import com.onyxdb.mdb.core.clusters.models.MongoV8d0Config;
import com.onyxdb.mdb.core.clusters.models.MongodV8d0;
import com.onyxdb.mdb.core.clusters.models.MongodV8d0Config;
import com.onyxdb.mdb.core.clusters.models.MongodV8d0Net;
import com.onyxdb.mdb.generated.openapi.models.V1ClusterResources;
import com.onyxdb.mdb.generated.openapi.models.V1CreateMongoClusterRequest;
import com.onyxdb.mdb.generated.openapi.models.V1MongoConfig;
import com.onyxdb.mdb.generated.openapi.models.V1MongoConfigV8d0;
import com.onyxdb.mdb.generated.openapi.models.V1MongodConfigV8d0;
import com.onyxdb.mdb.generated.openapi.models.V1MongodV8d0;

/**
 * @author foxleren
 */
public class ClusterMapper {
    public CreateCluster v1CreateMongoClusterRequestToCreateCluster(V1CreateMongoClusterRequest r) {
        V1MongoConfig v1MongoConfig = r.getConfig();
        return new CreateCluster(
                r.getName(),
                r.getDescription(),
                r.getProjectId(),
                ClusterType.MONGODB,
                ClusterVersion.fromStringVersionWithDots(ClusterType.MONGODB, v1MongoConfig.getVersion()),
                ClusterConfig.builder()
                        .withMongoV8d0(v1MongoConfigV8d0ToMongoV8d0Config(v1MongoConfig.getMongodbV8d0()))
                        .build()
        );
    }

    public MongoV8d0Config v1MongoConfigV8d0ToMongoV8d0Config(V1MongoConfigV8d0 c) {
        return new MongoV8d0Config(
                v1MongodV8d0ToMongodV8d0(c.getMongod())
        );
    }

    public MongodV8d0 v1MongodV8d0ToMongodV8d0(V1MongodV8d0 m) {
        return new MongodV8d0(
                v1ClusterResourcesToClusterResources(m.getResources()),
                v1MongodConfigV8d0ToMongodV8d0Config(m.getConfig())
        );
    }

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
                c.version(),
                c.config()
        );
    }

    public com.onyxdb.mdb.generated.jooq.enums.ClusterType clusterTypeToJooqClusterType(ClusterType t) {
        return com.onyxdb.mdb.generated.jooq.enums.ClusterType.lookupLiteral(t.value());
    }

    public com.onyxdb.mdb.generated.jooq.enums.ClusterVersion clusterVersionToJooqClusterVersion(ClusterVersion v) {
        return com.onyxdb.mdb.generated.jooq.enums.ClusterVersion.lookupLiteral(v.value());
    }

    public MongodV8d0Config v1MongodConfigV8d0ToMongodV8d0Config(V1MongodConfigV8d0 c) {
        return new MongodV8d0Config(
                new MongodV8d0Net(
                        c.getNet().getMaxIncomingConnections()
                )
        );
    }
}
