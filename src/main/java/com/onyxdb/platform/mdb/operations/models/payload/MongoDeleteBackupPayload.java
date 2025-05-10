package com.onyxdb.platform.mdb.operations.models.payload;

import java.util.UUID;

public record MongoDeleteBackupPayload(
        UUID clusterId,
        String backupName
) implements Payload {
}
