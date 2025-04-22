package com.onyxdb.platform.core.clusters.models;

import java.util.UUID;

public record UpdateCluster(
        UUID id,
        String description,
        ClusterConfig config
) {
}
