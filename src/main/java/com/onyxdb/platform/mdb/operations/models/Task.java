package com.onyxdb.platform.mdb.operations.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

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
        LocalDateTime updatedAt,
        int attemptsLeft,
        String payload,
        List<UUID> blockerIds,
        int postDelaySeconds,
        int retryDelaySeconds,
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
            int attemptsLeft,
            String payload,
            List<UUID> blockerIds,
            int postDelaySeconds,
            int retryDelaySeconds
    ) {
        LocalDateTime now = TimeUtils.now();
        return new Task(
                id,
                type,
                status,
                operationId,
                now,
                now,
                attemptsLeft,
                payload,
                blockerIds,
                postDelaySeconds,
                retryDelaySeconds,
                null,
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
        private LocalDateTime updatedAt;
        private int attemptsLeft;
        private String payload;
        private List<UUID> blockerIds;
        private int postDelaySeconds;
        private int retryDelaySeconds;
        private LocalDateTime startedAt;
        private LocalDateTime finishedAt;

        public Builder copy(Task task) {
            this.id = task.id;
            this.type = task.type;
            this.status = task.status;
            this.operationId = task.operationId;
            this.createdAt = task.createdAt;
            this.updatedAt = task.updatedAt;
            this.attemptsLeft = task.attemptsLeft;
            this.payload = task.payload;
            this.blockerIds = task.blockerIds;
            this.postDelaySeconds = task.postDelaySeconds;
            this.retryDelaySeconds = task.retryDelaySeconds;
            this.startedAt = task.startedAt;
            this.finishedAt = task.finishedAt;
            return this;
        }

        public Builder status(TaskStatus status) {
            this.status = status;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder attemptsLeft(int attempts) {
            this.attemptsLeft = attempts;
            return this;
        }

        public Builder postDelaySeconds(int postDelaySeconds) {
            this.postDelaySeconds = postDelaySeconds;
            return this;
        }

        public Builder retryDelaySeconds(int retryDelaySeconds) {
            this.retryDelaySeconds = retryDelaySeconds;
            return this;
        }

        public Builder startedAt(LocalDateTime startedAt) {
            this.startedAt = startedAt;
            return this;
        }

        public Builder finishedAt(LocalDateTime finishedAt) {
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
                    updatedAt,
                    attemptsLeft,
                    payload,
                    blockerIds,
                    postDelaySeconds,
                    retryDelaySeconds,
                    startedAt,
                    finishedAt
            );
        }
    }
}
