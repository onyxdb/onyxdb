package com.onyxdb.mdb.repositories;

import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.onyxdb.mdb.generated.jooq.tables.Clusters;
import com.onyxdb.mdb.generated.jooq.tables.records.ClustersRecord;
import com.onyxdb.mdb.models.Cluster;

import static com.onyxdb.mdb.generated.jooq.Tables.CLUSTERS;

/**
 * @author foxleren
 */
@Repository
@RequiredArgsConstructor
public class ClusterPostgresRepository implements ClusterRepository {
    private static final Clusters CLUSTERS_TABLE = CLUSTERS;

    private final DSLContext dslContext;

    @Override
    public void create(Cluster cluster) {
        dslContext.executeInsert(cluster.toJooqClustersRecord());
    }

    @Override
    public Optional<Cluster> getByIdO(UUID id) {
        return dslContext.select(
                        CLUSTERS_TABLE.ID,
                        CLUSTERS_TABLE.NAME,
                        CLUSTERS_TABLE.DESCRIPTION,
                        CLUSTERS_TABLE.TYPE
                )
                .from(CLUSTERS_TABLE)
                .where(CLUSTERS_TABLE.ID.eq(id))
                .fetchOptional()
                .map(r -> r.into(ClustersRecord.class))
                .map(Cluster::fromJooqClustersRecord);
    }
}
