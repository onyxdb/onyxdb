package com.onyxdb.mdb.core.clusters.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

public record User(
        UUID id,
        String name,
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
}
