package com.onyxdb.platform.mdb.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.onyxdb.platform.mdb.utils.TimeUtils;

public record Database(
        UUID id,
        String name,
        UUID clusterId,
        LocalDateTime createdAt,
        UUID createdBy,
        boolean isDeleted,
        @Nullable
        LocalDateTime deletedAt,
        @Nullable
        UUID deletedBy
) {
    public static Database create(
            String name,
            UUID clusterId,
            UUID createdBy
    ) {
        return new Database(
                UUID.randomUUID(),
                name,
                clusterId,
                TimeUtils.now(),
                createdBy,
                false,
                null,
                null
        );
    }
}
