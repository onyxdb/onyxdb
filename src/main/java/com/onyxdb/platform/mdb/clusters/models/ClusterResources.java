package com.onyxdb.platform.mdb.clusters.models;

import java.util.Objects;

/**
 * @author foxleren
 */
public record ClusterResources(
        String presetId,
        String storageClass,
        long storage
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String presetId;
        private String storageClass;
        private long storage;

        public Builder copy(ClusterResources resources) {
            this.presetId = resources.presetId;
            this.storageClass = resources.storageClass;
            this.storage = resources.storage;
            return this;
        }

        public Builder overrideWithUpdateClusterResources(UpdateClusterResources updateClusterResources) {
            this.presetId = updateClusterResources.presetId();
            return this;
        }

        public ClusterResources build() {
            return new ClusterResources(
                    Objects.requireNonNull(presetId),
                    Objects.requireNonNull(storageClass),
                    storage
            );
        }
    }
}
