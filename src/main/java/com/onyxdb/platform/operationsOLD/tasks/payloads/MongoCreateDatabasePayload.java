package com.onyxdb.platform.operationsOLD.tasks.payloads;

import java.util.UUID;

import com.onyxdb.platform.processing.models.payloads.Payload;

public record MongoCreateDatabasePayload(
        UUID clusterId,
        String databaseName
) implements Payload {
}
