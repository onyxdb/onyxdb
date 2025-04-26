package com.onyxdb.platform.core.clusters.models;

import java.util.List;
import java.util.UUID;

public record UserToCreate(
        UUID clusterId,
        String username,
        String password,
        List<MongoPermissionToCreate> permissions
) {
}
