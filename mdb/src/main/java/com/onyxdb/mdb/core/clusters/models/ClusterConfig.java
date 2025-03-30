package com.onyxdb.mdb.core.clusters.models;

/**
 * @author foxleren
 */
public record ClusterConfig(
//        MongoV8d0Config mongoV8d0
        ClusterResources resources,
        int replicas
) {
//    public static Builder builder() {
//        return new Builder();
//    }
//
//    public static class Builder {
//        private MongoV8d0Config mongoV8d0;
//
//        public Builder withMongoV8d0(MongoV8d0Config mongoV8d0) {
//            this.mongoV8d0 = mongoV8d0;
//            return this;
//        }
//
//        public ClusterConfig build() {
//            return new ClusterConfig(
//                    mongoV8d0
//            );
//        }
//    }
}
