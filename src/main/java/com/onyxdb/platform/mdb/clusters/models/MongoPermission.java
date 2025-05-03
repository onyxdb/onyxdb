package com.onyxdb.platform.mdb.clusters.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

public record MongoPermission(
        UUID id,
        String userName,
        String databaseName,
        UUID clusterId,
        LocalDateTime createdAt,
        UUID createdBy,
        boolean isDeleted,
        @Nullable
        LocalDateTime deletedAt,
        @Nullable
        UUID deletedBy,
        PermissionData data
) {
}
