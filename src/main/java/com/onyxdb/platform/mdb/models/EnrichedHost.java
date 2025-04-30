package com.onyxdb.platform.mdb.models;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;

public record EnrichedHost(
        String name,
        UUID clusterId,
        @Nullable
        String type,
        @Nullable
        String status,
        @Nullable
        String role
) {
    public static EnrichedHost withoutMeta(
            String name,
            UUID clusterId
    ) {
        return new EnrichedHost(
                name,
                clusterId,
                null,
                null,
                null
        );
    }
}
