package com.onyxdb.onyxdbApi.models;

import com.onyxdb.onyxdbApi.generated.openapi.models.V1CreateClusterRequestStorage;

/**
 * @author foxleren
 */
public record ClusterStorage(long diskSize) {
    public static ClusterStorage fromV1CreateClusterRequestStorage(V1CreateClusterRequestStorage storage) {
        return new ClusterStorage(storage.getDiskSize());
    }
}
