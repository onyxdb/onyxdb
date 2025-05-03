package com.onyxdb.platform.mdb.scheduling.operations.models.payloads;

import java.util.UUID;

public record MongoDeleteUserPayload(
        UUID clusterId,
        UUID userId,
        String username
) implements Payload {
}
