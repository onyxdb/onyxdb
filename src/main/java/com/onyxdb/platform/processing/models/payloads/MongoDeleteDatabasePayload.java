package com.onyxdb.platform.processing.models.payloads;

import java.util.UUID;

public record MongoDeleteDatabasePayload(
        UUID clusterId,
        UUID databaseId,
        String databaseName
) implements Payload {
}
