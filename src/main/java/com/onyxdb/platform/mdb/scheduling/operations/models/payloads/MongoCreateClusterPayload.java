package com.onyxdb.platform.mdb.scheduling.operations.models.payloads;

import java.util.List;
import java.util.UUID;

import com.onyxdb.platform.mdb.clusters.models.MongoRole;

public record MongoCreateClusterPayload(
        UUID clusterId,
        String databaseName,
        String userName,
        String passwordSecretName,
        String passwordSecretNamespace,
        List<MongoRole> roles
) implements Payload {
}
