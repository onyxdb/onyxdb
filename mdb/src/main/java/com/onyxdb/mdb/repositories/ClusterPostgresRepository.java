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
    public void createCluster(Cluster cluster) {
        var clusterTable = com.onyxdb.mdb.generated.jooq.tables.Cluster.CLUSTER;
        dslContext.insertInto(clusterTable)
                .set(clusterTable.ID, cluster.id())
                .set(clusterTable.NAME, cluster.name())
                .set(clusterTable.DESCRIPTION, cluster.description())
                .execute();
    }
}
