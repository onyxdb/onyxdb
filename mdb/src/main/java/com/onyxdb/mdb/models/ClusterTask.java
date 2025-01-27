package com.onyxdb.mdb.models;

import java.time.LocalDateTime;
import java.util.List;
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
        int retriesLeft,
        List<UUID> dependsOnTaskIds,
        boolean isLast)
{
    public ClusterTasksRecord toJooqClusterTasksRecord() {
        return new ClusterTasksRecord(
                id,
                clusterId,
                operationId,
                com.onyxdb.mdb.generated.jooq.enums.ClusterType.valueOf(clusterType.getValue()),
                com.onyxdb.mdb.generated.jooq.enums.ClusterTaskType.valueOf(type.getValue()),
                com.onyxdb.mdb.generated.jooq.enums.ClusterTaskStatus.valueOf(status.getValue()),
                createdAt,
                updatedAt,
                scheduledAt,
                retriesLeft,
                dependsOnTaskIds.toArray(UUID[]::new),
                isLast
        );
    }

    public static ClusterTask scheduled(
            UUID clusterId,
            UUID operationId,
            ClusterType clusterType,
            ClusterTaskType type,
            LocalDateTime scheduledAt,
            int retriesLeft,
            List<UUID> dependsOnTaskIds,
            boolean isLast)
    {
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
                retriesLeft,
                dependsOnTaskIds,
                isLast
        );
    }

    public static ClusterTask scheduledNotLast(
            UUID clusterId,
            UUID operationId,
            ClusterType clusterType,
            ClusterTaskType type,
            LocalDateTime scheduledAt,
            int retriesLeft,
            List<UUID> dependsOnTaskIds)
    {
        return scheduled(
                clusterId,
                operationId,
                clusterType,
                type,
                scheduledAt,
                retriesLeft,
                dependsOnTaskIds,
                false
        );
    }

    public static ClusterTask scheduledLast(
            UUID clusterId,
            UUID operationId,
            ClusterType clusterType,
            ClusterTaskType type,
            LocalDateTime scheduledAt,
            int retriesLeft,
            List<UUID> dependsOnTaskIds)
    {
        return scheduled(
                clusterId,
                operationId,
                clusterType,
                type,
                scheduledAt,
                retriesLeft,
                dependsOnTaskIds,
                true
        );
    }

}
