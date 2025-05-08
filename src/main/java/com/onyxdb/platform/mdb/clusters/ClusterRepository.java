package com.onyxdb.platform.mdb.clusters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.clusters.models.ClusterFilter;

/**
 * @author foxleren
 */
public interface ClusterRepository {
    List<Cluster> listClusters(ClusterFilter filter);

    void createCluster(Cluster cluster);

    Optional<Cluster> getClusterO(UUID clusterId);

    Cluster getClusterOrThrow(UUID clusterId);

    void markClusterDeleted(UUID clusterId);

    void updateCluster(Cluster cluster);
}
