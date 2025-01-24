package com.onyxdb.mdb.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.onyxdb.mdb.generated.jooq.enums.ClusterTaskStatus;
import com.onyxdb.mdb.generated.jooq.tables.records.ClusterOperationRecord;
import com.onyxdb.mdb.utils.CommonUtils;

/**
 * @author foxleren
 */
public record ClusterOperation(
        UUID id,
        UUID clusterId,
        ClusterType clusterType,
        ClusterTaskType type,
        ClusterTaskStatus status,
        LocalDateTime createdAt,
        int retries,
        int maxRetries,
        LocalDateTime execute_at,
        List<UUID> dependsOn)
{
//    public static ClusterOperation create(
//            UUID clusterId,
//            ClusterTaskType type,
//            ClusterTaskStatus status,
//            LocalDateTime createdAt)
//    {
//        return new ClusterOperation(
//                UUID.randomUUID(),
//                clusterId,
//                type,
//                status,
//                LocalDateTime.now(),
//                0,
//                LocalDateTime.now()
//        );
//    }

    public ClusterOperationRecord toJooqClusterOperationRecord() {
        return new ClusterOperationRecord(
                id,
                clusterId,
                com.onyxdb.mdb.generated.jooq.enums.ClusterOperationType.valueOf(type.toString()),
                ClusterTaskStatus.valueOf(status.toString()),
                createdAt,
                retries,
                execute_at
        );
    }

    private static LocalDateTime generateExecuteAt() {
        return LocalDateTime.now().plusSeconds(CommonUtils.getRandomNumber(0, 180));
    }
}
