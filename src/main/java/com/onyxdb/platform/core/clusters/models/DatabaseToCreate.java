package com.onyxdb.platform.core.clusters.models;

import java.util.UUID;

public record DatabaseToCreate(
        UUID clusterId,
        String databaseName,
        UUID createdBy
) {
}
