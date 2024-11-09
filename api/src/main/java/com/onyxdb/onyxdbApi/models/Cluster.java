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
        ClusterResources resources,
        ClusterSpec spec)
{
    public static Cluster fromV1CreateClusterRequest(V1CreateClusterRequest request) {
        return new Cluster(
                UUID.randomUUID(),
                request.getName(),
                request.getDescription(),
                ClusterResources.fromApiV1ClusterResources(request.getResources()),
                ClusterSpec.fromApiV1ClusterSpec(request.getSpec())
        );
    }
}
