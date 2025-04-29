package com.onyxdb.platform.operationsOLD.tasks.payloads;

import java.util.List;
import java.util.UUID;

import com.onyxdb.platform.clients.onyxdbAgent.models.MongoPermissionDTO;
import com.onyxdb.platform.processing.models.payloads.Payload;

public record MongoCreateUserPayload(
        UUID clusterId,
        String username,
        String passwordSecretName,
        String passwordSecretNamespace,
        List<MongoPermissionDTO> permissions
) implements Payload {
}
