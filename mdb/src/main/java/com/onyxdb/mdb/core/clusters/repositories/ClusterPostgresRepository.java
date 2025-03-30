package com.onyxdb.mdb.core.clusters.repositories;

import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.DSLContext;
import org.jooq.JSONB;

import com.onyxdb.mdb.core.clusters.ClusterMapper;
import com.onyxdb.mdb.core.clusters.models.Cluster;

import static com.onyxdb.mdb.generated.jooq.Tables.CLUSTERS;

/**
 * @author foxleren
 */
public class ClusterPostgresRepository implements ClusterRepository {
    private final DSLContext dslContext;
    private final ClusterMapper clusterMapper;
    private final ObjectMapper objectMapper;


    public ClusterPostgresRepository(
            DSLContext dslContext,
            ClusterMapper clusterMapper,
            ObjectMapper objectMapper
    ) {
        this.dslContext = dslContext;
        this.clusterMapper = clusterMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public void createCluster(Cluster cluster) {
        try {
            dslContext.insertInto(CLUSTERS)
                    .columns(
                            CLUSTERS.ID,
                            CLUSTERS.NAME,
                            CLUSTERS.DESCRIPTION,
                            CLUSTERS.PROJECT_ID,
                            CLUSTERS.TYPE,
                            CLUSTERS.CONFIG
                    )
                    .values(
                            cluster.id(),
                            cluster.name(),
                            cluster.description(),
                            cluster.projectId(),
                            clusterMapper.clusterTypeToJooqClusterType(cluster.type()),
                            JSONB.jsonb(objectMapper.writeValueAsString(cluster.config()))
                    )
                    .execute();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Cluster> getClusterO(UUID clusterId) {
        return dslContext.select()
                .from(CLUSTERS)
                .where(CLUSTERS.ID.eq(clusterId))
                .fetchOptional()
                .map(clusterMapper::fromJooqRecord);
    }
}
