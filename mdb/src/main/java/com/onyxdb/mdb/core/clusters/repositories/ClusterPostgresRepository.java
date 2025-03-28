package com.onyxdb.mdb.core.clusters.repositories;

import org.jooq.DSLContext;

import com.onyxdb.mdb.core.clusters.ClusterMapper;
import com.onyxdb.mdb.core.clusters.models.Cluster;

import static com.onyxdb.mdb.generated.jooq.Tables.CLUSTERS;

/**
 * @author foxleren
 */
public abstract class ClusterPostgresRepository implements ClusterRepository {
    protected final DSLContext dslContext;
    protected final ClusterMapper clusterMapper;

    public ClusterPostgresRepository(
            DSLContext dslContext,
            ClusterMapper clusterMapper
    ) {
        this.dslContext = dslContext;
        this.clusterMapper = clusterMapper;
    }

    @Override
    public void createCluster(Cluster cluster) {
        dslContext.insertInto(CLUSTERS)
                .columns(
                        CLUSTERS.ID,
                        CLUSTERS.NAME,
                        CLUSTERS.DESCRIPTION,
                        CLUSTERS.PROJECT_ID,
                        CLUSTERS.TYPE,
                        CLUSTERS.VERSION
                )
                .values(
                        cluster.id(),
                        cluster.name(),
                        cluster.description(),
                        cluster.projectId(),
                        clusterMapper.clusterTypeToJooqClusterType(cluster.type()),
                        clusterMapper.clusterVersionToJooqClusterVersion(cluster.version())
                )
                .execute();
    }
}
