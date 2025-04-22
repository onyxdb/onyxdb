package com.onyxdb.platform.taskProcessing.models.payloads;

import java.util.UUID;

public record ClusterTaskPayload(
        UUID clusterId
) implements TaskPayload {
}
