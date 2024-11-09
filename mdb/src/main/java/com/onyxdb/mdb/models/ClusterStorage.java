package com.onyxdb.mdb.models;

import com.onyxdb.mdb.generated.openapi.models.V1CreateClusterRequestResourcesStorage;

/**
 * @author foxleren
 */
public record ClusterStorage(long diskSizeBytes) {
    public static ClusterStorage fromApiV1ClusterStorage(V1CreateClusterRequestResourcesStorage storage) {
        return new ClusterStorage(storage.getDiskSizeBytes());
    }
}
