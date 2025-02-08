package com.onyxdb.mdb.models;

import java.util.UUID;

import com.onyxdb.mdb.generated.openapi.models.V1CreateClusterRequest;

/**
 * @author foxleren
 */
public record ClusterToCreate(
        String name,
        UUID projectId
) {
    public static ClusterToCreate fromV1CreateClusterRequest(V1CreateClusterRequest rq) {
        return new ClusterToCreate(
                rq.getName(),
                rq.getProjectId()
        );
    }
}
