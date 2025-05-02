package com.onyxdb.platform.mdb.models;

import java.util.List;
import java.util.UUID;

public record CreateMongoPermission(
        String userName,
        String databaseName,
        UUID clusterId,
        List<MongoRole> roles
) {
}
