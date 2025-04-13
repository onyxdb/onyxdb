package com.onyxdb.mdb.core.clusters.models;

import java.util.UUID;

/**
 * @author foxleren
 */
// TODO store namespace
// TODO add status
public record Cluster(
        UUID id,
        String name,
        String description,
        UUID projectId,
        ClusterType type,
        ClusterConfig config
) {
}
