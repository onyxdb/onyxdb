package com.onyxdb.platform.mdb.clusters.models;

import java.util.UUID;

public record CreateClusterResult(
        UUID clusterId,
        UUID operationId
) {
}
