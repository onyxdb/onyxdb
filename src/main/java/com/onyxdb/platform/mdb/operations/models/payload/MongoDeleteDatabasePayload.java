package com.onyxdb.platform.mdb.operations.models.payload;

import java.util.UUID;

public record MongoDeleteDatabasePayload(
        UUID clusterId,
        UUID databaseId,
        String databaseName
) implements Payload {
}
