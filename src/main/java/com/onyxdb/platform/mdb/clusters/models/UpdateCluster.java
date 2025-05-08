package com.onyxdb.platform.mdb.clusters.models;

import java.util.UUID;

public record UpdateCluster(
        UUID id,
        String description,
        UpdateClusterConfig config,
        UUID updatedBy
) {
}
