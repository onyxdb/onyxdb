package com.onyxdb.mdb.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.mdb.generated.jooq.tables.records.ClusterOperationsRecord;

/**
 * @author foxleren
 */
public record ClusterOperation(
        UUID id,
        UUID clusterId,
        ClusterOperationType type,
        ClusterOperationStatus status,
        LocalDateTime createdAt,
        UUID createdBy,
        LocalDateTime updatedAt)
{
    public static ClusterOperation scheduled(
            UUID clusterId,
            ClusterOperationType type,
            UUID createdBy)
    {
        return new ClusterOperation(
                UUID.randomUUID(),
                clusterId,
                type,
                ClusterOperationStatus.IN_PROGRESS,
                LocalDateTime.now(),
                createdBy,
                LocalDateTime.now()
        );
    }

    public ClusterOperationsRecord toJooqClusterOperationsRecord() {
        return new ClusterOperationsRecord(
                id,
                clusterId,
                com.onyxdb.mdb.generated.jooq.enums.ClusterOperationType.valueOf(type.getValue()),
                com.onyxdb.mdb.generated.jooq.enums.ClusterOperationStatus.valueOf(status.getValue()),
                createdAt,
                createdBy,
                updatedAt
        );
    }
}
