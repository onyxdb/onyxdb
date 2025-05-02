package com.onyxdb.platform.mdb.operationsOLD.payloads;

import java.util.UUID;

import com.onyxdb.platform.mdb.models.CreateUser;

public class CreateMongoUserOperationPayload extends ClusterOperationPayload {
    public final CreateUser createUser;

    public CreateMongoUserOperationPayload(UUID clusterId, CreateUser createUser) {
        super(clusterId);
        this.createUser = createUser;
    }
}
