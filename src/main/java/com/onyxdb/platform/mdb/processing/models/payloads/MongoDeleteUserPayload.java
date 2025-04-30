package com.onyxdb.platform.mdb.processing.models.payloads;

import java.util.UUID;

public record MongoDeleteUserPayload(
        UUID clusterId,
        UUID userId,
        String username
) implements Payload {
}
