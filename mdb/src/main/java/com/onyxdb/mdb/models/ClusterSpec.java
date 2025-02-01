package com.onyxdb.mdb.models;

import org.springframework.lang.Nullable;

/**
 * @author foxleren
 */
public record ClusterSpec(
        ClusterType type,
        @Nullable
        ClusterMongodbSpec mongodbSpec)
{
//    public static ClusterSpec fromApiV1ClusterSpec(V1CreateClusterRequestSpec clusterSpec) {
//        String rawClusterType = clusterSpec.getType().getValue();
//        Optional<ClusterType> clusterTypeO = ClusterType.parseO(rawClusterType);
//        ClusterType clusterType = clusterTypeO.orElseThrow(() -> new InvalidClusterTypeException(rawClusterType));
//
//        return new ClusterSpec(
//                clusterType,
//                clusterType == ClusterType.MONGODB ?
//                        ClusterMongodbSpec.fromApiV1ClusterSpecMongodb(clusterSpec.getMongodb()) :
//                        null
//        );
//    }
}
