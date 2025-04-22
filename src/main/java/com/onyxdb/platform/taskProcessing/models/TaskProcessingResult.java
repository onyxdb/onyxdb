package com.onyxdb.platform.taskProcessing.models;

import java.time.LocalDateTime;

import org.jetbrains.annotations.Nullable;

public record TaskProcessingResult(
        TaskStatus status,
        @Nullable
        LocalDateTime scheduledAt
) {
    public static TaskProcessingResult scheduled(LocalDateTime scheduledAt) {
        return new TaskProcessingResult(
                TaskStatus.SCHEDULED,
                scheduledAt
        );
    }

    public static TaskProcessingResult success() {
        return new TaskProcessingResult(
                TaskStatus.SUCCESS,
                null
        );
    }
}
