package com.onyxdb.platform.mdb.operations.models.payload;

import java.util.UUID;

import com.onyxdb.platform.mdb.clusters.models.UpdateClusterConfig;

public record MongoModifyClusterPayload(
        UUID clusterId,
        UpdateClusterConfig clusterConfig
) implements Payload {
}
