package com.onyxdb.platform.mdb.clusters.models;

import java.time.LocalDateTime;
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
}
