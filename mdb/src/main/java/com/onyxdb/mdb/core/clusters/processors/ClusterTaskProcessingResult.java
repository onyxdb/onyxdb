package com.onyxdb.mdb.core.clusters.processors;

import java.time.LocalDateTime;

import org.jetbrains.annotations.Nullable;

import com.onyxdb.mdb.core.clusters.models.ClusterTaskStatus;

public record ClusterTaskProcessingResult(
        ClusterTaskStatus status,
        @Nullable
        LocalDateTime scheduledAt
) {
    public static ClusterTaskProcessingResult scheduled(LocalDateTime scheduledAt) {
        return new ClusterTaskProcessingResult(
                ClusterTaskStatus.SCHEDULED,
                scheduledAt
        );
    }

    public static ClusterTaskProcessingResult success() {
        return new ClusterTaskProcessingResult(
                ClusterTaskStatus.SUCCESS,
                null
        );
    }
}
