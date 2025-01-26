package com.onyxdb.mdb.models;

import java.util.UUID;

import com.onyxdb.mdb.generated.jooq.tables.records.ClustersRecord;
import com.onyxdb.mdb.generated.openapi.models.V1GetClusterResponse;

/**
 * @author foxleren
 */
public record Cluster(
        UUID id,
        String name,
        String description,
        ClusterType type)
{
    public ClustersRecord toJooqClustersRecord() {
        return new ClustersRecord(
                id,
                name,
                description,
                com.onyxdb.mdb.generated.jooq.enums.ClusterType.valueOf(type.getValue())
        );
    }

    public V1GetClusterResponse toV1GetClusterResponse() {
        return new V1GetClusterResponse(
                id,
                name,
                description,
                V1GetClusterResponse.TypeEnum.fromValue(type.getValue())
        );
    }

    public static Cluster fromJooqClustersRecord(ClustersRecord r) {
        return new Cluster(
                r.getId(),
                r.getName(),
                r.getDescription(),
                ClusterType.fromValue(r.getType().getLiteral())
        );
    }

    public static Cluster fromClusterToCreate(ClusterToCreate c) {
        return new Cluster(
                UUID.randomUUID(),
                c.name(),
                c.description(),
                ClusterType.MONGODB
        );
    }
}
