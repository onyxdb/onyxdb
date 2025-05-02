package com.onyxdb.platform.mdb.processing.models.payloads;

import java.util.UUID;

import com.onyxdb.platform.mdb.clusters.models.ClusterConfig;

public record MongoScaleClusterPayload(
        UUID clusterId,
        ClusterConfig clusterConfig
) implements Payload {
}
