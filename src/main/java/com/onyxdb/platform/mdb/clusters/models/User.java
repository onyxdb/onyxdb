package com.onyxdb.platform.mdb.clusters.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

public record User(
        UUID id,
        String name,
        String passwordSecretName,
        UUID clusterId,
        LocalDateTime createdAt,
        UUID createdBy,
        boolean isDeleted,
        @Nullable
        LocalDateTime deletedAt,
        @Nullable
        UUID deletedBy,
        List<MongoPermission> permissions
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String name;
        private String passwordSecretName;
        private UUID clusterId;
        private LocalDateTime createdAt;
        private UUID createdBy;
        private boolean isDeleted;
        private LocalDateTime deletedAt;
        private UUID deletedBy;
        private List<MongoPermission> permissions;

        public Builder copy(User user) {
            this.id = user.id;
            this.name = user.name;
            this.passwordSecretName = user.passwordSecretName;
            this.clusterId = user.clusterId;
            this.createdAt = user.createdAt;
            this.createdBy = user.createdBy;
            this.isDeleted = user.isDeleted;
            this.deletedAt = user.deletedAt;
            this.deletedBy = user.deletedBy;
            this.permissions = user.permissions;
            return this;
        }

        public Builder permissions(List<MongoPermission> permissions) {
            this.permissions = permissions;
            return this;
        }

        public User build() {
            return new User(
                    id,
                    name,
                    passwordSecretName,
                    clusterId,
                    createdAt,
                    createdBy,
                    isDeleted,
                    deletedAt,
                    deletedBy,
                    permissions
            );
        }
    }
}
