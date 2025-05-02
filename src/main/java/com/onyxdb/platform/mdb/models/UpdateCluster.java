package com.onyxdb.platform.mdb.models;

import java.util.UUID;

import com.onyxdb.platform.mdb.clusters.models.ClusterConfig;

public record UpdateCluster(
        UUID id,
        String description,
        ClusterConfig config
) {
}
