package com.onyxdb.platform.taskProcessing.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.onyxdb.platform.utils.Consts;
import com.onyxdb.platform.utils.TimeUtils;

/**
 * @author foxleren
 */
public record Operation(
        UUID id,
        OperationType type,
        OperationStatus status,
        LocalDateTime createdAt,
        UUID createdBy,
        LocalDateTime updatedAt,
        @Nullable
        UUID clusterId
) {
    public boolean isRestartAllowed() {
        return status.equalsStringEnum(OperationStatus.ERROR);
    }

    public static Operation scheduled(
            OperationType type,
            UUID clusterId
    ) {
        return new Operation(
                UUID.randomUUID(),
                type,
                OperationStatus.SCHEDULED,
                TimeUtils.now(),
                Consts.USER_ID,
                TimeUtils.now(),
                clusterId
        );
    }
}
