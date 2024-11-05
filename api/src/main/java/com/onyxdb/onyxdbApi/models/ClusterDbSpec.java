package com.onyxdb.onyxdbApi.models;

import java.util.Optional;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.lang.Nullable;

import com.onyxdb.onyxdbApi.exceptions.InvalidClusterTypeException;
import com.onyxdb.onyxdbApi.generated.openapi.models.V1CreateClusterRequestDbSpec;

/**
 * @author foxleren
 */
public record ClusterDbSpec(
        ClusterType type,
        @Nullable
        ClusterMongoDbSpec mongoDbSpec)
{
    public static ClusterDbSpec fromV1CreateClusterRequestDbSpec(V1CreateClusterRequestDbSpec dbSpec) {
        String rawClusterType = dbSpec.getType().getValue();
        Optional<ClusterType> clusterTypeO = ClusterType.parseO(rawClusterType);
        ClusterType clusterType = clusterTypeO.orElseThrow(() -> new InvalidClusterTypeException(rawClusterType));

        return new ClusterDbSpec(
                clusterType,
                clusterType == ClusterType.MONGODB ?
                        ClusterMongoDbSpec.fromV1CreateClusterRequestDbSpecMongodb(dbSpec.getMongodb()) :
                        null
        );
    }
}
