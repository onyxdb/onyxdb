package com.onyxdb.platform.operationsOLD.payloads;

import java.util.UUID;

import com.onyxdb.platform.core.clusters.models.UserToCreate;

public class CreateMongoUserOperationPayload extends ClusterOperationPayload {
    public final UserToCreate userToCreate;

    public CreateMongoUserOperationPayload(UUID clusterId, UserToCreate userToCreate) {
        super(clusterId);
        this.userToCreate = userToCreate;
    }
}
