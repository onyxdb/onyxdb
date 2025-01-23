package com.onyxdb.mdb.models;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import com.onyxdb.mdb.generated.jooq.enums.ClusterOperationStatus;
import com.onyxdb.mdb.generated.jooq.tables.records.ClusterOperationRecord;
import com.onyxdb.mdb.utils.CommonUtils;

/**
 * @author foxleren
 */
public record ClusterOperation(
        UUID id,
        UUID clusterId,
        ClusterType clusterType,
        ClusterOperationType type,
        ClusterOperationStatus status,
        LocalDateTime createdAt,
        int retries,
        LocalDateTime execute_at)
{
    public static ClusterOperation create(
            UUID clusterId,
            ClusterOperationType type,
            ClusterOperationStatus status,
            LocalDateTime createdAt)
    {
        return new ClusterOperation(
                UUID.randomUUID(),
                clusterId,
                type,
                status,
                LocalDateTime.now(),
                0,
                LocalDateTime.now()
        );
    }

    public ClusterOperationRecord toJooqClusterOperationRecord() {
        return new ClusterOperationRecord(
                id,
                clusterId,
                com.onyxdb.mdb.generated.jooq.enums.ClusterOperationType.valueOf(type.toString()),
                com.onyxdb.mdb.generated.jooq.enums.ClusterOperationStatus.valueOf(status.toString()),
                createdAt,
                retries,
                execute_at
        );
    }

    private static LocalDateTime generateExecuteAt() {
        return LocalDateTime.now().plusSeconds(CommonUtils.getRandomNumber(0, 180));
    }
}
