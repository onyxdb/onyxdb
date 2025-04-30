package com.onyxdb.platform.mdb.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

public record MongoPermission(
        UUID id,
        UUID userId,
        UUID databaseId,
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
