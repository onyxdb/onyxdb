package com.onyxdb.onyxdbApi.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.onyxdb.onyxdbApi.models.Cluster;

/**
 * @author foxleren
 */
@Repository
@RequiredArgsConstructor
public class ClusterPostgresRepository implements ClusterRepository {
    private final DSLContext dslContext;

    @Override
    public void createCluster(Cluster cluster) {
        var clustersTable = com.onyxdb.onyxdbApi.generated.jooq.tables.Clusters.CLUSTERS;
        dslContext.insertInto(clustersTable)
                .set(clustersTable.ID, cluster.id())
                .set(clustersTable.NAME, cluster.name())
                .execute();
    }
}
