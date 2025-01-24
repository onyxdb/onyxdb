package com.onyxdb.mdb.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author foxleren
 */
public record ClusterTask(
        UUID id,
        ClusterTaskType type,
        UUID clusterId,
        ClusterType clusterType,
        ClusterTaskStatus status,
        LocalDateTime createdAt,
        LocalDateTime executeAt,
        int retries,
        int maxRetries,
        List<UUID> dependsOnTasks,
        boolean isLast)
{
    public static ClusterTask create(
            ClusterTaskType type,
            UUID clusterId,
            ClusterType clusterType,
            LocalDateTime executeAt,
            int maxRetries,
            List<UUID> dependsOnTasks,
            boolean isLast)
    {
        return new ClusterTask(
                UUID.randomUUID(),
                type,
                clusterId,
                clusterType,
                ClusterTaskStatus.SCHEDULED,
                LocalDateTime.now(),
                executeAt,
                0,
                maxRetries,
                dependsOnTasks,
                isLast
        );
    }

    public static ClusterTask createNotLast(
            ClusterTaskType type,
            UUID clusterId,
            ClusterType clusterType,
            LocalDateTime executeAt,
            int maxRetries,
            List<UUID> dependsOnTasks)
    {
        return create(
                type,
                clusterId,
                clusterType,
                executeAt,
                maxRetries,
                dependsOnTasks,
                false
        );
    }

    public static ClusterTask createLast(
            ClusterTaskType type,
            UUID clusterId,
            ClusterType clusterType,
            LocalDateTime executeAt,
            int maxRetries,
            List<UUID> dependsOnTasks)
    {
        return create(
                type,
                clusterId,
                clusterType,
                executeAt,
                maxRetries,
                dependsOnTasks,
                true
        );
    }
}
