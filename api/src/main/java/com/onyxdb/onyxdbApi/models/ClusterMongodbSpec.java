package com.onyxdb.onyxdbApi.models;

import java.util.Optional;

import org.springframework.lang.Nullable;

import com.onyxdb.onyxdbApi.exceptions.InvalidMongoDbVersionException;
import com.onyxdb.onyxdbApi.generated.openapi.models.V1CreateClusterRequestSpecMongodb;

/**
 * @author foxleren
 */
public record ClusterMongodbSpec(
        MongodbVersion version,
        @Nullable
        MongodbV5x0Spec mongodbV5x0Spec,
        @Nullable
        MongodbV6x0Spec mongodbV6x0Spec)
{
    public static ClusterMongodbSpec fromApiV1ClusterSpecMongodb(
            V1CreateClusterRequestSpecMongodb mongodbSpec)
    {
        String rawVersion = mongodbSpec.getVersion().getValue();
        Optional<MongodbVersion> versionO = MongodbVersion.parseO(rawVersion);
        MongodbVersion version = versionO.orElseThrow(() -> new InvalidMongoDbVersionException(rawVersion));

        return new ClusterMongodbSpec(
                version,
                version == MongodbVersion.V5_0 ? new MongodbV5x0Spec() : null,
                version == MongodbVersion.V6_0 ? new MongodbV6x0Spec() : null
        );
    }
}
