package com.onyxdb.mdb.core.clusters.models;

import java.util.UUID;

/**
 * @author foxleren
 */
public record CreateCluster(
        String name,
        String description,
        UUID projectId,
        String namespace,
        ClusterType type,
        ClusterConfig config
) {
}
