package com.onyxdb.platform.processing.models.payloads;

import java.util.UUID;

import com.onyxdb.platform.core.clusters.models.ClusterConfig;

public record MongoScaleClusterPayload(
        UUID clusterId,
        ClusterConfig clusterConfig
) implements Payload {
}
