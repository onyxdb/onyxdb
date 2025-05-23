package com.onyxdb.platform.mdb.operations.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.onyxdb.platform.mdb.utils.TimeUtils;

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
        String payload,
        @Nullable
        UUID clusterId
) {
    public boolean isRestartAllowed() {
        return status.equalsStringEnum(OperationStatus.ERROR);
    }

    public static Operation scheduledWithPayload(
            OperationType type,
            UUID clusterId,
            UUID createdBy,
            String payload
    ) {
        return new Operation(
                UUID.randomUUID(),
                type,
                OperationStatus.SCHEDULED,
                TimeUtils.now(),
                createdBy,
                TimeUtils.now(),
                payload,
                clusterId
        );
    }
}
