package com.onyxdb.platform.mdb.models;

import java.util.List;
import java.util.UUID;

public record CreateUserWithSecret(
        String userName,
        String passwordSecretName,
        UUID clusterId,
        List<CreateMongoPermission> permissions
) {
}
