package com.onyxdb.platform.mdb.operations.models.payload;

import java.util.UUID;

public record ClusterPayload(
        UUID clusterId
) implements Payload {
}
