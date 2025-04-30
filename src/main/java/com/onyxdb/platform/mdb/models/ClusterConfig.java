package com.onyxdb.platform.mdb.models;

/**
 * @author foxleren
 */
public record ClusterConfig(
        ClusterResources resources,
        int replicas
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ClusterResources resources;
        private int replicas;

        public Builder copyFrom(ClusterConfig clusterConfig) {
            this.resources = clusterConfig.resources;
            this.replicas = clusterConfig.replicas;
            return this;
        }

//        public Builder overrideWith(ClusterConfig clusterConfig) {
//
//        }

        public Builder withReplicas(int replicas) {
            this.replicas = replicas;
            return this;
        }

        public ClusterConfig build() {
            return new ClusterConfig(
                    resources,
                    replicas
            );
        }
    }
}
