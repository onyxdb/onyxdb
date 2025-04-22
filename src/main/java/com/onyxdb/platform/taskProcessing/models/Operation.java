package com.onyxdb.platform.taskProcessing.models;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author foxleren
 */
public record Operation(
        UUID id,
        OperationType type,
        OperationStatus status,
        LocalDateTime createdAt
) {
    public static Operation scheduled(OperationType type) {
        return new Operation(
                UUID.randomUUID(),
                type,
                OperationStatus.SCHEDULED,
                LocalDateTime.now()
        );
    }
}
