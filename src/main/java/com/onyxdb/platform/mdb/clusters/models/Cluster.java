package com.onyxdb.platform.mdb.clusters.models;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.onyxdb.platform.mdb.utils.TimeUtils;

/**
 * @author foxleren
 */
public record Cluster(
        UUID id,
        String name,
        String description,
        ClusterStatus status,
        UUID projectId,
        String namespace,
        ClusterType type,
        ClusterConfig config,
        LocalDateTime createdAt,
        UUID createdBy,
        boolean isDeleted,
        @Nullable
        LocalDateTime deletedAt,
        @Nullable
        UUID deletedBy
) {
    public static Cluster create(
            UUID id,
            String name,
            String description,
            UUID projectId,
            String namespace,
            ClusterType type,
            ClusterConfig config,
            UUID createdBy
    ) {
        return new Cluster(
                id,
                name,
                description,
                ClusterStatus.CREATING,
                projectId,
                namespace,
                type,
                config,
                TimeUtils.now(),
                createdBy,
                false,
                null,
                null
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String name;
        private String description;
        private ClusterStatus status;
        private UUID projectId;
        private String namespace;
        private ClusterType type;
        private ClusterConfig config;
        private LocalDateTime createdAt;
        private UUID createdBy;
        private boolean isDeleted;
        private LocalDateTime deletedAt;
        private UUID deletedBy;

        public Builder copy(Cluster cluster) {
            this.id = cluster.id;
            this.name = cluster.name;
            this.description = cluster.description;
            this.status = cluster.status;
            this.projectId = cluster.projectId;
            this.namespace = cluster.namespace;
            this.type = cluster.type;
            this.config = cluster.config;
            this.createdAt = cluster.createdAt;
            this.createdBy = cluster.createdBy;
            this.isDeleted = cluster.isDeleted;
            this.deletedAt = cluster.deletedAt;
            this.deletedBy = cluster.deletedBy;
            return this;
        }

        public Builder status(ClusterStatus status) {
            this.status = status;
            return this;
        }

        public Builder isDeleted(boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public Builder deletedAt(LocalDateTime deletedAt) {
            this.deletedAt = deletedAt;
            return this;
        }

        public Builder deletedBy(UUID deletedBy) {
            this.deletedBy = deletedBy;
            return this;
        }

        public Builder overrideWithUpdateCluster(UpdateCluster updateCluster) {
            this.description = updateCluster.description();
            this.config = ClusterConfig.builder()
                    .copy(config)
                    .overrideWithUpdateClusterConfig(updateCluster.config())
                    .build();
            return this;
        }

        public Cluster build() {
            return new Cluster(
                    Objects.requireNonNull(id),
                    Objects.requireNonNull(name),
                    Objects.requireNonNull(description),
                    Objects.requireNonNull(status),
                    Objects.requireNonNull(projectId),
                    Objects.requireNonNull(namespace),
                    Objects.requireNonNull(type),
                    Objects.requireNonNull(config),
                    Objects.requireNonNull(createdAt),
                    Objects.requireNonNull(createdBy),
                    isDeleted,
                    deletedAt,
                    deletedBy
            );
        }
    }
}
