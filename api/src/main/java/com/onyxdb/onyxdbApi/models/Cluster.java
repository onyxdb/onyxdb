package com.onyxdb.onyxdbApi.models;

import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.onyxdbApi.generated.openapi.models.CreateClusterRequest;

/**
 * @author foxleren
 */
public record Cluster(
        UUID id,
        String name,
        String description,
        ClusterType type,
        Optional<Mongo6_0Config> s
)
{
    public static Cluster fromCreateClusterRequest(
            CreateClusterRequest request,
            ObjectMapper objectMapper)
    {
        var s = Optional.ofNullable(request.getDbConfig());
        var type = ClusterType.valueOf(request.getType().getValue());
        return new Cluster(
                UUID.randomUUID(),
                request.getName(),
                request.getDescription(),
                type,
                s.filter(v -> )
//                Op
        );
    }
}
