package com.onyxdb.platform.mdb.operationsOLD.tasks.payloads;

import java.util.UUID;

import com.onyxdb.platform.mdb.processing.models.payloads.Payload;

public record MongoCreateDatabasePayload(
        UUID clusterId,
        String databaseName
) implements Payload {
}
