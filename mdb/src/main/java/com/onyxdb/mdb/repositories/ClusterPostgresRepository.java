package com.onyxdb.mdb.repositories;

import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.onyxdb.mdb.generated.jooq.tables.records.ClustersRecord;
import com.onyxdb.mdb.models.Cluster;

import static com.onyxdb.mdb.generated.jooq.Tables.CLUSTERS;

/**
 * @author foxleren
 */
@Repository
@RequiredArgsConstructor
public class ClusterPostgresRepository implements ClusterRepository {
    private final DSLContext dslContext;

    @Override
    public void create(Cluster cluster) {
        dslContext.executeInsert(cluster.toJooqClustersRecord());
    }

    @Override
    public Optional<Cluster> getByIdO(UUID id) {
        return dslContext.select(
                        CLUSTERS.ID,
                        CLUSTERS.NAME,
                        CLUSTERS.TYPE
                )
                .from(CLUSTERS)
                .where(CLUSTERS.ID.eq(id))
                .fetchOptional()
                .map(r -> r.into(ClustersRecord.class))
                .map(Cluster::fromJooqClustersRecord);
    }
}
