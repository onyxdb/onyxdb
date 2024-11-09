package com.onyxdb.onyxdbApi.models;

import com.onyxdb.onyxdbApi.generated.openapi.models.V1CreateClusterRequestResourcesStorage;

/**
 * @author foxleren
 */
public record ClusterStorage(long diskSizeBytes) {
    public static ClusterStorage fromApiV1ClusterStorage(V1CreateClusterRequestResourcesStorage storage) {
        return new ClusterStorage(storage.getDiskSizeBytes());
    }
}
