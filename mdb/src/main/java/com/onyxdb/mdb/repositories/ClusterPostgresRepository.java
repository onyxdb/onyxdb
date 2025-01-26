package com.onyxdb.mdb.repositories;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.onyxdb.mdb.models.Cluster;

/**
 * @author foxleren
 */
@Repository
@RequiredArgsConstructor
public class ClusterPostgresRepository implements ClusterRepository {
    private final DSLContext dslContext;

    @Override
    public void create(Cluster cluster) {
        var t = com.onyxdb.mdb.generated.jooq.tables.Clusters.CLUSTERS;
//        dslContext.insertInto(
//                        t,
//                        t.ID,
//                        t.NAME,
//                        t.DESCRIPTION
//                )
//                .valuesOfRecords(cluster.toJooqClustersRecord())
//                .execute();
    }
}
