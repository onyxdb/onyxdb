package com.onyxdb.platform.mdb.operations.models.payload;

import java.time.LocalDateTime;
import java.util.UUID;

public record MongoCreateBackupPayload(
        UUID clusterId,
        LocalDateTime createdAt
) implements Payload {
}
