package com.onyxdb.mdb.models;

import java.util.UUID;

import com.onyxdb.mdb.generated.jooq.tables.records.ClustersRecord;
//import com.onyxdb.mdb.generated.openapi.models.V1GetClusterResponse;

/**
 * @author foxleren
 */
public record Cluster(
        UUID id,
        String name,
        UUID projectId,
        ClusterType type
) {
    public ClustersRecord toJooqClustersRecord() {
        return new ClustersRecord(
                id,
                name,
                projectId,
                com.onyxdb.mdb.generated.jooq.enums.ClusterType.valueOf(type.value())
        );
    }

//    public V1GetClusterResponse toV1GetClusterResponse() {
//        return new V1GetClusterResponse(
//                id,
//                name,
//                projectId,
//                V1GetClusterResponse.TypeEnum.fromValue(type.value())
//        );
//    }

    public static Cluster fromJooqClustersRecord(ClustersRecord r) {
        return new Cluster(
                r.getId(),
                r.getName(),
                r.getProjectId(),
                ClusterType.fromValue(r.getType().getLiteral())
        );
    }

    public static Cluster fromClusterToCreate(ClusterToCreate c) {
        return new Cluster(
                UUID.randomUUID(),
                c.name(),
                c.projectId(),
                ClusterType.MONGODB
        );
    }
}
