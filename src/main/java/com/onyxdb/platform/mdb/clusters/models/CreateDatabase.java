package com.onyxdb.platform.mdb.clusters.models;

import java.util.UUID;

public record CreateDatabase(
        String name,
        UUID clusterId,
        UUID createdBy
) {
}
