package com.onyxdb.onyxdbApi.models;

import java.util.UUID;

import com.onyxdb.onyxdbApi.generated.openapi.models.V1CreateClusterRequest;

/**
 * @author foxleren
 */
public record Cluster(
        UUID id,
        String name,
        String description,
        ClusterStorage storage,
        ClusterDbSpec dbSpec)
{
    public static Cluster fromV1CreateClusterRequest(V1CreateClusterRequest request) {
        return new Cluster(
                UUID.randomUUID(),
                request.getName(),
                request.getDescription(),
                ClusterStorage.fromV1CreateClusterRequestStorage(request.getStorage()),
                ClusterDbSpec.fromV1CreateClusterRequestDbSpec(request.getDbSpec())
        );
    }
}
