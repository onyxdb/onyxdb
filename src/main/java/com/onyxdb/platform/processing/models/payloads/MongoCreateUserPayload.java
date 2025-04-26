package com.onyxdb.platform.processing.models.payloads;

import java.util.List;
import java.util.UUID;

import com.onyxdb.platform.core.clusters.models.MongoPermissionToCreate;

public record MongoCreateUserPayload(
        UUID clusterId,
        String username,
        String passwordSecretName,
        String passwordSecretNamespace,
        List<MongoPermissionToCreate> permissions
) implements Payload {
}
