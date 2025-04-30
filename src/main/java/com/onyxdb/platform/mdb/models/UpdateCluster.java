package com.onyxdb.platform.mdb.models;

import java.util.UUID;

public record UpdateCluster(
        UUID id,
        String description,
        ClusterConfig config
) {
}
