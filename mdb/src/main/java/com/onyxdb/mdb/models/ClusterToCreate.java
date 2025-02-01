package com.onyxdb.mdb.models;

import com.onyxdb.mdb.generated.openapi.models.V1CreateClusterRequest;

/**
 * @author foxleren
 */
public record ClusterToCreate(
        String name,
        String description)
{
    public static ClusterToCreate fromV1CreateClusterRequest(V1CreateClusterRequest rq) {
        return new ClusterToCreate(
                rq.getName(),
                rq.getDescription()
        );
    }
}
