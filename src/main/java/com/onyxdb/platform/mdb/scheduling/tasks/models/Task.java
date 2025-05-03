package com.onyxdb.platform.mdb.scheduling.tasks.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import org.jooq.JSONB;

import com.onyxdb.platform.generated.jooq.tables.records.TasksRecord;
import com.onyxdb.platform.mdb.utils.TimeUtils;

/**
 * @author foxleren
 */
public record Task(
        UUID id,
        TaskType type,
        TaskStatus status,
        UUID operationId,
        LocalDateTime createdAt,
        LocalDateTime scheduledAt,
        int attemptsLeft,
        String payload,
        @Nullable
        LocalDateTime startedAt,
        @Nullable
        LocalDateTime finishedAt
) {
    public static Task create(
            UUID id,
            TaskType type,
            TaskStatus status,
            UUID operationId,
            LocalDateTime scheduledAt,
            int attemptsLeft,
            String payload
    ) {
        return new Task(
                id,
                type,
                status,
                operationId,
                TimeUtils.now(),
                scheduledAt,
                attemptsLeft,
                payload,
                null,
                null
        );
    }

    public TasksRecord toJooqClusterTasksRecord() {
        return new TasksRecord(
                id,
                type.value(),
                com.onyxdb.platform.generated.jooq.enums.TaskStatus.valueOf(status.value()),
                operationId,
                createdAt,
                scheduledAt,
                attemptsLeft,
                JSONB.jsonb(payload),
                startedAt,
                finishedAt
        );
    }

    public static Task fromJooqClusterTasksRecord(TasksRecord r) {
        return new Task(
                r.getId(),
                TaskType.R.fromValue(r.getType()),
                TaskStatus.fromValue(r.getStatus().getLiteral()),
                r.getOperationId(),
                r.getCreatedAt(),
                r.getScheduledAt(),
                r.getAttemptsLeft(),
                r.getPayload().data(),
                r.getStartedAt(),
                r.getFinishedAt()
        );
    }

    public static Task scheduled(
            TaskType type,
            UUID operationId,
            LocalDateTime scheduledAt,
            int attemptsLeft,
            String payload
    ) {
        return new Task(
                UUID.randomUUID(),
                type,
                TaskStatus.SCHEDULED,
                operationId,
                LocalDateTime.now(),
                scheduledAt,
                attemptsLeft,
                payload,
                TimeUtils.now(),
                null
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
        private LocalDateTime scheduledAt;
        private int attemptsLeft;
        private String payload;
        private LocalDateTime startedAt;
        private LocalDateTime finishedAt;

        public Builder copy(Task task) {
            this.id = task.id;
            this.type = task.type;
            this.status = task.status;
            this.operationId = task.operationId;
            this.createdAt = task.createdAt;
            this.scheduledAt = task.scheduledAt;
            this.attemptsLeft = task.attemptsLeft;
            this.payload = task.payload;
            this.startedAt = task.startedAt;
            this.finishedAt = task.finishedAt;

            return this;
        }

        public Builder withStatus(TaskStatus status) {
            this.status = status;
            return this;
        }

        public Builder withScheduledAt(LocalDateTime scheduledAt) {
            this.scheduledAt = scheduledAt;
            return this;
        }

        public Builder withAttemptsLeft(int attemptsLeft) {
            this.attemptsLeft = attemptsLeft;
            return this;
        }

        public Builder withStartedAt(LocalDateTime startedAt) {
            this.startedAt = startedAt;
            return this;
        }

        public Builder withFinishedAt(LocalDateTime finishedAt) {
            this.finishedAt = finishedAt;
            return this;
        }

        public Task build() {
            return new Task(
                    id,
                    type,
                    status,
                    operationId,
                    createdAt,
                    scheduledAt,
                    attemptsLeft,
                    payload,
                    startedAt,
                    finishedAt
            );
        }
    }
}
