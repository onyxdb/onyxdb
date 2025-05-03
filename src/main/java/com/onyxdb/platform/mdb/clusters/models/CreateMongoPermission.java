package com.onyxdb.platform.mdb.clusters.models;

import java.util.List;
import java.util.UUID;

public record CreateMongoPermission(
        String userName,
        String databaseName,
        UUID clusterId,
        List<MongoRole> roles
) {
}
