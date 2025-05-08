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
        String namespace,
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
            String namespace,
            UUID createdBy
    ) {
        return new Project(
                UUID.randomUUID(),
                name,
                description,
                productId,
                namespace,
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
            String namespace,
            UUID createdBy
    ) {
        return new Project(
                id,
                name,
                description,
                productId,
                namespace,
                TimeUtils.now(),
                createdBy,
                false,
                null,
                null
        );
    }
}
