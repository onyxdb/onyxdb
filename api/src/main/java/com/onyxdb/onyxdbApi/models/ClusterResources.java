package com.onyxdb.onyxdbApi.models;

import com.onyxdb.onyxdbApi.generated.openapi.models.V1CreateClusterRequestResources;

/**
 * @author foxleren
 */
public record ClusterResources(ClusterStorage storage) {
    public static ClusterResources fromApiV1ClusterResources(V1CreateClusterRequestResources resources) {
        return new ClusterResources(
                ClusterStorage.fromApiV1ClusterStorage(resources.getStorage())
        );
    }
}
