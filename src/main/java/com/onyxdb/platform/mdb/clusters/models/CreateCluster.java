package com.onyxdb.platform.mdb.clusters.models;

import java.util.UUID;

/**
 * @author foxleren
 */
public record CreateCluster(
        String name,
        String description,
        UUID projectId,
        ClusterType type,
        ClusterConfig config,
        String databaseName,
        String userName,
        String password,
        UUID createdBy
) {
}
