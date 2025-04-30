package com.onyxdb.platform.mdb.clusters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.DSLContext;
import org.jooq.JSONB;

import com.onyxdb.platform.mdb.exceptions.ClusterNotFoundException;
import com.onyxdb.platform.mdb.models.Cluster;
import com.onyxdb.platform.mdb.models.ClusterConfig;
import com.onyxdb.platform.mdb.models.UpdateCluster;

import static com.onyxdb.platform.generated.jooq.Tables.CLUSTERS;

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
    public List<Cluster> listClusters() {
        return dslContext.select()
                .from(CLUSTERS)
                .where(CLUSTERS.IS_DELETED.eq(false))
                .fetch(clusterMapper::fromJooqRecord);
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

    @Override
    public Cluster getCluster(UUID clusterId) {
        return getClusterO(clusterId).orElseThrow(() -> new ClusterNotFoundException(clusterId));
    }

    @Override
    public void markClusterDeleted(UUID clusterId) {
        dslContext.update(CLUSTERS)
                .set(CLUSTERS.IS_DELETED, true)
                .where(CLUSTERS.ID.eq(clusterId))
                .execute();
    }

    @Override
    public void updateClusterConfig(UUID clusterId, ClusterConfig config) {
        try {
            dslContext.update(CLUSTERS)
                    .set(CLUSTERS.CONFIG, JSONB.jsonb(objectMapper.writeValueAsString(config)))
                    .where(CLUSTERS.ID.eq(clusterId))
                    .execute();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCluster(UpdateCluster updateCluster) {
        try {
            dslContext.update(CLUSTERS)
                    .set(CLUSTERS.DESCRIPTION, updateCluster.description())
                    .set(CLUSTERS.CONFIG, JSONB.jsonb(objectMapper.writeValueAsString(updateCluster.config())))
                    .where(CLUSTERS.ID.eq(updateCluster.id()))
                    .execute();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
