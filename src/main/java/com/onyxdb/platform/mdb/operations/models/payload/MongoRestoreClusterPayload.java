package com.onyxdb.platform.mdb.operations.models.payload;

import java.util.UUID;

public record MongoRestoreClusterPayload(
        UUID clusterId,
        String backupName
) implements Payload {
}
