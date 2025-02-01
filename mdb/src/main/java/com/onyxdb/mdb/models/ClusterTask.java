package com.onyxdb.mdb.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.mdb.generated.jooq.tables.records.ClusterTasksRecord;

/**
 * @author foxleren
 */
public record ClusterTask(
        UUID id,
        UUID clusterId,
        UUID operationId,
        ClusterType clusterType,
        ClusterTaskType type,
        ClusterTaskStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime scheduledAt,
        int attemptsLeft,
        boolean isFirst,
        boolean isLast
) {
    public ClusterTasksRecord toJooqClusterTasksRecord() {
        return new ClusterTasksRecord(
                id,
                clusterId,
                operationId,
                com.onyxdb.mdb.generated.jooq.enums.ClusterType.valueOf(clusterType.value()),
                com.onyxdb.mdb.generated.jooq.enums.ClusterTaskType.valueOf(type.value()),
                com.onyxdb.mdb.generated.jooq.enums.ClusterTaskStatus.valueOf(status.value()),
                createdAt,
                updatedAt,
                scheduledAt,
                attemptsLeft,
                isFirst,
                isLast
        );
    }

    public static ClusterTask fromJooqClusterTasksRecord(ClusterTasksRecord r) {
        return new ClusterTask(
                r.getId(),
                r.getClusterId(),
                r.getOperationId(),
                ClusterType.fromValue(r.getClusterType().getLiteral()),
                ClusterTaskType.fromValue(r.getType().getLiteral()),
                ClusterTaskStatus.fromValue(r.getStatus().getLiteral()),
                r.getCreatedAt(),
                r.getUpdatedAt(),
                r.getScheduledAt(),
                r.getAttemptsLeft(),
                r.getIsFirst(),
                r.getIsLast()
        );
    }

    public static ClusterTask scheduled(
            UUID clusterId,
            UUID operationId,
            ClusterType clusterType,
            ClusterTaskType type,
            LocalDateTime scheduledAt,
            int attemptsLeft,
            boolean isFirst,
            boolean isLast
    ) {
        return new ClusterTask(
                UUID.randomUUID(),
                clusterId,
                operationId,
                clusterType,
                type,
                ClusterTaskStatus.SCHEDULED,
                LocalDateTime.now(),
                LocalDateTime.now(),
                scheduledAt,
                attemptsLeft,
                isFirst,
                isLast
        );
    }

    public static ClusterTask scheduledFirst(
            UUID clusterId,
            UUID operationId,
            ClusterType clusterType,
            ClusterTaskType type,
            LocalDateTime scheduledAt,
            int attemptsLeft
    ) {
        return scheduled(
                clusterId,
                operationId,
                clusterType,
                type,
                scheduledAt,
                attemptsLeft,
                true,
                false
        );
    }

    public static ClusterTask scheduledLast(
            UUID clusterId,
            UUID operationId,
            ClusterType clusterType,
            ClusterTaskType type,
            LocalDateTime scheduledAt,
            int attemptsLeft
    ) {
        return scheduled(
                clusterId,
                operationId,
                clusterType,
                type,
                scheduledAt,
                attemptsLeft,
                false,
                true
        );
    }

    public static ClusterTask scheduledMiddle(
            UUID clusterId,
            UUID operationId,
            ClusterType clusterType,
            ClusterTaskType type,
            LocalDateTime scheduledAt,
            int attemptsLeft
    ) {
        return scheduled(
                clusterId,
                operationId,
                clusterType,
                type,
                scheduledAt,
                attemptsLeft,
                false,
                false
        );
    }
}
