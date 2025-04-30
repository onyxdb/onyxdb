package com.onyxdb.platform.mdb.clusters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.platform.mdb.models.Cluster;
import com.onyxdb.platform.mdb.models.ClusterConfig;
import com.onyxdb.platform.mdb.models.UpdateCluster;

/**
 * @author foxleren
 */
public interface ClusterRepository {
    List<Cluster> listClusters();

    void createCluster(Cluster cluster);

    Optional<Cluster> getClusterO(UUID clusterId);

    Cluster getCluster(UUID clusterId);

    void markClusterDeleted(UUID clusterId);

    void updateClusterConfig(UUID clusterId, ClusterConfig config);

    void updateCluster(UpdateCluster updateCluster);
}
