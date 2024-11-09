package com.onyxdb.onyxdbApi.models;

import java.util.Optional;

import org.springframework.lang.Nullable;

import com.onyxdb.onyxdbApi.exceptions.InvalidClusterTypeException;
import com.onyxdb.onyxdbApi.generated.openapi.models.V1CreateClusterRequestSpec;

/**
 * @author foxleren
 */
public record ClusterSpec(
        ClusterType type,
        @Nullable
        ClusterMongodbSpec mongodbSpec)
{
    public static ClusterSpec fromApiV1ClusterSpec(V1CreateClusterRequestSpec clusterSpec) {
        String rawClusterType = clusterSpec.getType().getValue();
        Optional<ClusterType> clusterTypeO = ClusterType.parseO(rawClusterType);
        ClusterType clusterType = clusterTypeO.orElseThrow(() -> new InvalidClusterTypeException(rawClusterType));

        return new ClusterSpec(
                clusterType,
                clusterType == ClusterType.MONGODB ?
                        ClusterMongodbSpec.fromApiV1ClusterSpecMongodb(clusterSpec.getMongodb()) :
                        null
        );
    }
}
