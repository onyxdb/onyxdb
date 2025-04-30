package com.onyxdb.platform.mdb.operationsOLD.payloads;

import java.util.UUID;

import com.onyxdb.platform.mdb.models.UserToCreate;

public class CreateMongoUserOperationPayload extends ClusterOperationPayload {
    public final UserToCreate userToCreate;

    public CreateMongoUserOperationPayload(UUID clusterId, UserToCreate userToCreate) {
        super(clusterId);
        this.userToCreate = userToCreate;
    }
}
