package com.onyxdb.platform.processing.models.payloads;

import java.util.List;
import java.util.UUID;

import com.onyxdb.platform.core.clusters.models.MongoRole;

public record MongoCreateClusterPayload(
        UUID clusterId,
        UUID databaseId,
        String databaseName,
        String username,
        String passwordSecretName,
        String passwordSecretNamespace,
        List<MongoRole> roles
) implements Payload {
}
