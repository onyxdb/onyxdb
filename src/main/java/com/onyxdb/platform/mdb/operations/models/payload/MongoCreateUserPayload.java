package com.onyxdb.platform.mdb.operations.models.payload;

import java.util.List;
import java.util.UUID;

import com.onyxdb.platform.mdb.clusters.models.CreateMongoPermission;

public record MongoCreateUserPayload(
        UUID clusterId,
        String username,
        String passwordSecretName,
        String passwordSecretNamespace,
        List<CreateMongoPermission> permissions
) implements Payload {
}
