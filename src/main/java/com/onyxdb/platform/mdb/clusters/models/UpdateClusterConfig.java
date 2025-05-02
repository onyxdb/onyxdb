package com.onyxdb.platform.mdb.clusters.models;

/**
 * @author foxleren
 */
public record UpdateClusterConfig(
        ClusterVersion version,
        UpdateClusterResources resources,
        int replicas,
        ClusterBackupConfig backup
) {
}
