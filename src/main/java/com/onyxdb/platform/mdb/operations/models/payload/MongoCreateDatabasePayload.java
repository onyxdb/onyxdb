package com.onyxdb.platform.mdb.operations.models.payload;

import java.util.UUID;

public record MongoCreateDatabasePayload(
        UUID clusterId,
        String databaseName
) implements Payload {
}
