package com.onyxdb.platform.mdb.operations.models.payload;

import java.util.List;
import java.util.UUID;

import com.onyxdb.platform.mdb.models.MongoRole;

public record MongoCreateClusterPayload(
        UUID clusterId,
        String databaseName,
        String userName,
        String passwordSecretName,
        String passwordSecretNamespace,
        List<MongoRole> roles
) implements Payload {
}
