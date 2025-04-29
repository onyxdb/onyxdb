package com.onyxdb.platform.processing.models.payloads;

import java.util.UUID;

public record ClusterPayload(
        UUID clusterId
) implements Payload {
}
