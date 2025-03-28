package com.onyxdb.mdb.core.clusters.models;

import java.util.UUID;

/**
 * @author foxleren
 */
public record Cluster(
        UUID id,
        String name,
        String description,
        UUID projectId,
        ClusterType type,
        ClusterVersion version,
        ClusterConfig config
) {
}
