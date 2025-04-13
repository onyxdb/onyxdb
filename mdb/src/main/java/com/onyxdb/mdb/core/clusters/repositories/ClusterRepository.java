package com.onyxdb.mdb.core.clusters.repositories;

import java.util.Optional;
import java.util.UUID;

import com.onyxdb.mdb.core.clusters.models.Cluster;
import com.onyxdb.mdb.core.clusters.models.ClusterConfig;
import com.onyxdb.mdb.core.clusters.models.UpdateCluster;

/**
 * @author foxleren
 */
public interface ClusterRepository {
    void createCluster(Cluster cluster);

    Optional<Cluster> getClusterO(UUID clusterId);

    void markClusterDeleted(UUID clusterId);

    void updateClusterConfig(UUID clusterId, ClusterConfig config);

    void updateCluster(UpdateCluster updateCluster);
}
