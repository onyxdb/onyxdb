package com.onyxdb.platform.operationsOLD.payloads;

import java.util.UUID;

public class ClusterOperationPayload extends EmptyOperationPayload {
    public final UUID clusterId;

    public ClusterOperationPayload(UUID clusterId) {
        this.clusterId = clusterId;
    }
}
