package com.onyxdb.platform.mdb.scheduling.operations.models.payloads;

import java.util.UUID;

public record MongoCreateDatabasePayload(
        UUID clusterId,
        String databaseName
) implements Payload {
}
