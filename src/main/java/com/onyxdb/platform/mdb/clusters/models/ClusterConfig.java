package com.onyxdb.platform.mdb.clusters.models;

/**
 * @author foxleren
 */
public record ClusterConfig(
        ClusterVersion version,
        ClusterResources resources,
        int replicas,
        ClusterBackupConfig backup
) {
//    public static Builder builder() {
//        return new Builder();
//    }
//
//    public static class Builder {
//        private ClusterResources resources;
//        private int replicas;
//
//        public Builder copyFrom(ClusterConfig clusterConfig) {
//            this.resources = clusterConfig.resources;
//            this.replicas = clusterConfig.replicas;
//            return this;
//        }
//
////        public Builder overrideWith(ClusterConfig clusterConfig) {
////
////        }
//
//        public Builder withReplicas(int replicas) {
//            this.replicas = replicas;
//            return this;
//        }
//
//        public ClusterConfig build() {
//            return new ClusterConfig(
//                    resources,
//                    replicas
//            );
//        }
//    }
}
