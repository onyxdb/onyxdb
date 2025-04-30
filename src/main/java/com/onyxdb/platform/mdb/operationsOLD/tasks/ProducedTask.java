package com.onyxdb.platform.mdb.operationsOLD.tasks;

import java.util.List;
import java.util.UUID;

import com.onyxdb.platform.mdb.processing.models.TaskType;
import com.onyxdb.platform.mdb.processing.models.payloads.EmptyPayload;
import com.onyxdb.platform.mdb.processing.models.payloads.Payload;

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
