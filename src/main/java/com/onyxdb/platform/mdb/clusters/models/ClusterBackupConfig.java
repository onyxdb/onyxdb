package com.onyxdb.platform.mdb.clusters.models;

public record ClusterBackupConfig(
        boolean isEnabled,
        String schedule,
        int limit
) {
}
