package com.onyxdb.mdb.core.clusters.models;

import java.util.List;
import java.util.UUID;

public record UserToCreate(
        String name,
        String password,
        UUID clusterId,
        List<MongoPermissionToCreate> permissions
) {
}
