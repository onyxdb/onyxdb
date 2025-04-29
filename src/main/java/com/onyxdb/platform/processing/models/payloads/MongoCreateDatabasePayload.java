package com.onyxdb.platform.processing.models.payloads;

import java.util.UUID;

public record MongoCreateDatabasePayload(
        UUID clusterId,
        String databaseName
) implements Payload {
}
