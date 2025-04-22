package com.onyxdb.platform.core.clusters.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.platform.core.clusters.models.Cluster;
import com.onyxdb.platform.core.clusters.models.ClusterConfig;
import com.onyxdb.platform.core.clusters.models.UpdateCluster;

/**
 * @author foxleren
 */
public interface ClusterRepository {
    List<Cluster> listClusters();

    void createCluster(Cluster cluster);

    Optional<Cluster> getClusterO(UUID clusterId);

    void markClusterDeleted(UUID clusterId);

    void updateClusterConfig(UUID clusterId, ClusterConfig config);

    void updateCluster(UpdateCluster updateCluster);
}
