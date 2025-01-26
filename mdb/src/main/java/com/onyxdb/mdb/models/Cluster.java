package com.onyxdb.mdb.models;

import java.util.UUID;

import com.onyxdb.mdb.generated.jooq.tables.records.ClustersRecord;

/**
 * @author foxleren
 */
public record Cluster(
        UUID id,
        String name,
        String description,
        ClusterType type)
{
//    public ClustersRecord toJooqClustersRecord() {
//        return new ClustersRecord(
//                id,
//                name,
//                description
//        );
//    }

    public static Cluster fromClusterToCreate(ClusterToCreate clusterToCreate) {
        return new Cluster(
                UUID.randomUUID(),
                clusterToCreate.name(),
                clusterToCreate.description(),
                ClusterType.MONGODB
        );
    }
}
