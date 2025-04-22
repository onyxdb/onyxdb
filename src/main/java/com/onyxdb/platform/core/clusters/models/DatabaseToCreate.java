package com.onyxdb.platform.core.clusters.models;

import java.util.UUID;

public record DatabaseToCreate(
        String name,
        UUID clusterId,
        UUID createdBy
) {
}
