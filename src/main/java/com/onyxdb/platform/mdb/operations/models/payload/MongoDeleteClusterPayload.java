package com.onyxdb.platform.mdb.operations.models.payload;

import java.util.UUID;

public record MongoDeleteClusterPayload(
        UUID clusterId,
        UUID deletedBy
) implements Payload {
}
