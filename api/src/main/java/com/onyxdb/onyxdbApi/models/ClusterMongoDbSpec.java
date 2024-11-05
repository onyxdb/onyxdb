package com.onyxdb.onyxdbApi.models;

import java.util.Optional;

import org.springframework.lang.Nullable;

import com.onyxdb.onyxdbApi.exceptions.InvalidMongoDbVersionException;
import com.onyxdb.onyxdbApi.generated.openapi.models.V1CreateClusterRequestDbSpecMongodb;

/**
 * @author foxleren
 */
public record ClusterMongoDbSpec(
        MongoDbVersion version,
        @Nullable
        MongoDbV5x0Spec v5x0Spec,
        @Nullable
        MongoDbV6x0Spec v6x0Spec)
{
    public static ClusterMongoDbSpec fromV1CreateClusterRequestDbSpecMongodb(
            V1CreateClusterRequestDbSpecMongodb mongoDbSpec)
    {
        String rawVersion = mongoDbSpec.getVersion().getValue();
        Optional<MongoDbVersion> versionO = MongoDbVersion.parseO(rawVersion);
        MongoDbVersion version = versionO.orElseThrow(() -> new InvalidMongoDbVersionException(rawVersion));

        return new ClusterMongoDbSpec(
                version,
                version == MongoDbVersion.V5_0 ? new MongoDbV5x0Spec() : null,
                version == MongoDbVersion.V6_0 ? new MongoDbV6x0Spec() : null
        );
    }
}
