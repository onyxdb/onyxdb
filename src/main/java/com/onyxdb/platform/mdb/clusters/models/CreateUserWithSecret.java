package com.onyxdb.platform.mdb.clusters.models;

import java.util.List;
import java.util.UUID;

public record CreateUserWithSecret(
        String userName,
        String passwordSecretName,
        UUID clusterId,
        List<CreateMongoPermission> permissions
) {
}
