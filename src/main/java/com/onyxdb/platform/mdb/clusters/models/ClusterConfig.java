package com.onyxdb.platform.mdb.clusters.models;

import java.util.Objects;

/**
 * @author foxleren
 */
public record ClusterConfig(
        ClusterVersion version,
        ClusterResources resources,
        int replicas,
        ClusterBackupConfig backup
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ClusterVersion version;
        private ClusterResources resources;
        private int replicas;
        private ClusterBackupConfig backup;

        public Builder copy(ClusterConfig clusterConfig) {
            this.version = clusterConfig.version();
            this.resources = clusterConfig.resources();
            this.replicas = clusterConfig.replicas();
            this.backup = clusterConfig.backup();
            return this;
        }

        public Builder overrideWithUpdateClusterConfig(UpdateClusterConfig updateClusterConfig) {
            this.version = updateClusterConfig.version();
            this.resources = ClusterResources.builder().copy(resources)
                    .overrideWithUpdateClusterResources(updateClusterConfig.resources())
                    .build();
            this.replicas = updateClusterConfig.replicas();
            this.backup = updateClusterConfig.backup();
            return this;
        }

        public ClusterConfig build() {
            return new ClusterConfig(
                    Objects.requireNonNull(version),
                    Objects.requireNonNull(resources),
                    replicas,
                    Objects.requireNonNull(backup)
            );
        }
    }
}
