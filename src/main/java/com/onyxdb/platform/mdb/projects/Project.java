package com.onyxdb.platform.mdb.projects;

import java.time.LocalDateTime;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.onyxdb.platform.mdb.utils.TimeUtils;

/**
 * @author foxleren
 */
public record Project(
        UUID id,
        String name,
        String description,
        UUID productId,
        LocalDateTime createdAt,
        UUID createdBy,
        boolean isDeleted,
        @Nullable
        LocalDateTime deletedAt,
        @Nullable
        UUID deletedBy
) {
    public static Project create(
            String name,
            String description,
            UUID productId,
            UUID createdBy
    ) {
        return new Project(
                UUID.randomUUID(),
                name,
                description,
                productId,
                TimeUtils.now(),
                createdBy,
                false,
                null,
                null
        );
    }

    public static Project create(
            UUID id,
            String name,
            String description,
            UUID productId,
            UUID createdBy
    ) {
        return new Project(
                id,
                name,
                description,
                productId,
                TimeUtils.now(),
                createdBy,
                false,
                null,
                null
        );
    }
}
