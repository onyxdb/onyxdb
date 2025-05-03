package com.onyxdb.platform.mdb.operations.models.payload;

import java.util.UUID;

import com.onyxdb.platform.mdb.clusters.models.ClusterConfig;

public record MongoScaleClusterPayload(
        UUID clusterId,
        ClusterConfig clusterConfig
) implements Payload {
}
