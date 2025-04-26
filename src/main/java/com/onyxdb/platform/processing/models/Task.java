package com.onyxdb.platform.processing.models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.jooq.JSONB;

import com.onyxdb.platform.generated.jooq.tables.records.TasksRecord;

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
                type.value(),
                com.onyxdb.platform.generated.jooq.enums.TaskStatus.valueOf(status.value()),
                operationId,
                createdAt,
                updatedAt,
                scheduledAt,
                attemptsLeft,
                false,
                false,
//                isFirst,
//                isLast,
                JSONB.jsonb(payload)
        );
    }

    public static Task fromJooqClusterTasksRecord(TasksRecord r) {
        return new Task(
                r.getId(),
                TaskType.R.fromValue(r.getType()),
                TaskStatus.fromValue(r.getStatus().getLiteral()),
                r.getOperationId(),
                r.getCreatedAt(),
                r.getUpdatedAt(),
                r.getScheduledAt(),
                r.getAttemptsLeft(),
//                r.getIsFirst(),
//                r.getIsLast(),
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
//                isFirst,
//                isLast,
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private TaskType type;
        private TaskStatus status;
        private UUID operationId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime scheduledAt;
        private int attemptsLeft;
        private boolean isFirst;
        private boolean isLast;
        private String payload;

        public Builder copy(Task task) {
            this.id = task.id;
            this.type = task.type;
            this.status = task.status;
            this.operationId = task.operationId;
            this.createdAt = task.createdAt;
            this.updatedAt = task.updatedAt;
            this.scheduledAt = task.scheduledAt;
            this.attemptsLeft = task.attemptsLeft;
//            this.isFirst = task.isFirst;
//            this.isLast = task.isLast;
            this.payload = task.payload;

            return this;
        }

        public Builder withStatus(TaskStatus status) {
            this.status = status;
            return this;
        }

        public Builder withAttemptsLeft(int attemptsLeft) {
            this.attemptsLeft = attemptsLeft;
            return this;
        }

        public Task build() {
            return new Task(
                    id,
                    type,
                    status,
                    operationId,
                    createdAt,
                    updatedAt,
                    scheduledAt,
                    attemptsLeft,
//                    isFirst,
//                    isLast,
                    payload
            );
        }
    }
}
