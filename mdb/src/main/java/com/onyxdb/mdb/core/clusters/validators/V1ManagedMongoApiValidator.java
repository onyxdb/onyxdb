package com.onyxdb.mdb.core.clusters.validators;

import java.util.Objects;

import com.onyxdb.mdb.core.clusters.models.ClusterType;
import com.onyxdb.mdb.core.clusters.models.ClusterVersion;
import com.onyxdb.mdb.exceptions.InvalidClusterConfigException;
import com.onyxdb.mdb.generated.openapi.models.V1CreateMongoClusterRequest;
import com.onyxdb.mdb.generated.openapi.models.V1MongoConfig;

/**
 * @author foxleren
 */
public class V1ManagedMongoApiValidator {
    public void validateV1CreateMongoClusterRequest(V1CreateMongoClusterRequest r) {
        V1MongoConfig config = r.getConfig();
        ClusterVersion clusterVersion = ClusterVersion.fromStringVersionWithDots(
                ClusterType.MONGODB,
                config.getVersion()
        );
        if (clusterVersion == ClusterVersion.MONGODB_8_0) {
            if (Objects.isNull(config.getMongodbV8d0())) {
                throw new InvalidClusterConfigException(clusterVersion);
            }
        }
    }
}
