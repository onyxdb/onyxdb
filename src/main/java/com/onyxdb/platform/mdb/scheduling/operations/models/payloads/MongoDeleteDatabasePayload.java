package com.onyxdb.platform.mdb.scheduling.operations.models.payloads;

import java.util.UUID;

public record MongoDeleteDatabasePayload(
        UUID clusterId,
        UUID databaseId,
        String databaseName
) implements Payload {
}
