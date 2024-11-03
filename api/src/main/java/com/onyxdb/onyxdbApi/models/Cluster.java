package com.onyxdb.onyxdbApi.models;

import java.util.UUID;

import com.onyxdb.onyxdbApi.generated.openapi.models.CreateClusterRequest;

/**
 * @author foxleren
 */
public record Cluster(UUID id, String name) {
    public static Cluster fromCreateClusterRequest(CreateClusterRequest request) {
        return new Cluster(
                UUID.randomUUID(),
                request.getName()
        );
    }
}
