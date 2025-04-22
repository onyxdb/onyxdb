package com.onyxdb.platform.core.clusters.models;

import java.util.List;
import java.util.UUID;

public record MongoPermissionToCreate(
        UUID databaseId,
        List<MongoRole> roles
) {
}
