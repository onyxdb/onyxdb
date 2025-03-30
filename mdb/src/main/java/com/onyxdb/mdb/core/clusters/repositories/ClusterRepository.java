package com.onyxdb.mdb.core.clusters.repositories;

import java.util.Optional;
import java.util.UUID;

import com.onyxdb.mdb.core.clusters.models.Cluster;

/**
 * @author foxleren
 */
public interface ClusterRepository {
    void createCluster(Cluster cluster);

    Optional<Cluster> getClusterO(UUID clusterId);
}
