package com.onyxdb.platform.mdb.processing.models.payloads;

import java.util.UUID;

public record MongoCreateDatabasePayload(
        UUID clusterId,
        String databaseName
) implements Payload {
}
