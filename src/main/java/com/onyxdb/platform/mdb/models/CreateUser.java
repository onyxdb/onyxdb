package com.onyxdb.platform.mdb.models;

import java.util.List;
import java.util.UUID;

public record CreateUser(
        String userName,
        String password,
        UUID clusterId,
        List<CreateMongoPermission> permissions
) {
}
