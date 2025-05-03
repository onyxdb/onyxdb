package com.onyxdb.platform.mdb.operations.models.payload;

import java.util.UUID;

public record MongoDeleteUserPayload(
        UUID clusterId,
        UUID userId,
        String username
) implements Payload {
}
