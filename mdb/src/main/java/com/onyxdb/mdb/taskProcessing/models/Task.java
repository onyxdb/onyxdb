package com.onyxdb.mdb.taskProcessing.models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.jooq.JSONB;

import com.onyxdb.mdb.generated.jooq.tables.records.TasksRecord;

/**
 * @author foxleren
 */
public record Task(
        UUID id,
        TaskType type,
        TaskStatus status,
        UUID operationId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime scheduledAt,
        int attemptsLeft,
        boolean isFirst,
        boolean isLast,
        String payload
) {
    public boolean hasNoAttempts() {
        return attemptsLeft == 0;
    }

    public LocalDateTime getScheduledAtWithDelay(Duration delay) {
        return scheduledAt.plusSeconds(delay.getSeconds());
    }

    public TasksRecord toJooqClusterTasksRecord() {
        return new TasksRecord(
                id,
                com.onyxdb.mdb.generated.jooq.enums.TaskType.valueOf(type.value()),
                com.onyxdb.mdb.generated.jooq.enums.TaskStatus.valueOf(status.value()),
                operationId,
                createdAt,
                updatedAt,
                scheduledAt,
                attemptsLeft,
                isFirst,
                isLast,
                JSONB.jsonb(payload)
        );
    }

    public static Task fromJooqClusterTasksRecord(TasksRecord r) {
        return new Task(
                r.getId(),
                TaskType.fromValue(r.getType().getLiteral()),
                TaskStatus.fromValue(r.getStatus().getLiteral()),
                r.getOperationId(),
                r.getCreatedAt(),
                r.getUpdatedAt(),
                r.getScheduledAt(),
                r.getAttemptsLeft(),
                r.getIsFirst(),
                r.getIsLast(),
                r.getPayload().data()
        );
    }

    public static Task scheduled(
            TaskType type,
            UUID operationId,
            LocalDateTime scheduledAt,
            int attemptsLeft,
            boolean isFirst,
            boolean isLast,
            String payload
    ) {
        return new Task(
                UUID.randomUUID(),
                type,
                TaskStatus.SCHEDULED,
                operationId,
                LocalDateTime.now(),
                LocalDateTime.now(),
                scheduledAt,
                attemptsLeft,
                isFirst,
                isLast,
                payload
        );
    }

    public static Task scheduledFirst(
            TaskType type,
            UUID operationId,
            LocalDateTime scheduledAt,
            int attemptsLeft,
            String payload
    ) {
        return scheduled(
                type,
                operationId,
                scheduledAt,
                attemptsLeft,
                true,
                false,
                payload
        );
    }

    public static Task scheduledLast(
            TaskType type,
            UUID operationId,
            LocalDateTime scheduledAt,
            int attemptsLeft,
            String payload
    ) {
        return scheduled(
                type,
                operationId,
                scheduledAt,
                attemptsLeft,
                false,
                true,
                payload
        );
    }

    public static Task scheduledMiddle(
            TaskType type,
            UUID operationId,
            LocalDateTime scheduledAt,
            int attemptsLeft,
            String payload
    ) {
        return scheduled(
                type,
                operationId,
                scheduledAt,
                attemptsLeft,
                false,
                false,
                payload
        );
    }
}
