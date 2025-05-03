package com.onyxdb.platform.mdb.operations.models;

import java.util.List;
import java.util.UUID;

import com.onyxdb.platform.mdb.operations.models.payload.EmptyPayload;
import com.onyxdb.platform.mdb.operations.models.payload.Payload;

public record ProducedTask(
        UUID id,
        TaskType type,
        UUID operationId,
        List<UUID> blockerIds,
        Payload payload
) {

    public static ProducedTask create(
            TaskType type,
            UUID operationId,
            List<UUID> blockerIds
    ) {
        return new ProducedTask(
                UUID.randomUUID(),
                type,
                operationId,
                blockerIds,
                new EmptyPayload()
        );
    }

    public static ProducedTask createWithPayload(
            TaskType type,
            UUID operationId,
            List<UUID> blockerIds,
            Payload payload
    ) {
        return new ProducedTask(
                UUID.randomUUID(),
                type,
                operationId,
                blockerIds,
                payload
        );
    }
}
