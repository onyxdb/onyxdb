package com.onyxdb.platform.mdb.scheduling.operations.models.payloads;

import java.util.UUID;

public record ClusterPayload(
        UUID clusterId
) implements Payload {
}
