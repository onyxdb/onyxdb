package com.onyxdb.mdb.core.clusters.models;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author foxleren
 */
public record ClusterOperation(
        UUID id,
        UUID clusterId,
        ClusterType clusterType,
        ClusterOperationType type,
        ClusterOperationStatus status,
        LocalDateTime createdAt
) {
    public static ClusterOperation scheduled(
            UUID clusterId,
            ClusterType clusterType,
            ClusterOperationType type
    ) {
        return new ClusterOperation(
                UUID.randomUUID(),
                clusterId,
                clusterType,
                type,
                ClusterOperationStatus.SCHEDULED,
                LocalDateTime.now()
        );
    }
}
