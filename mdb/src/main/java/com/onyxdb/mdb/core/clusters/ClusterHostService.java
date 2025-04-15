package com.onyxdb.mdb.core.clusters;

import java.util.List;

import com.onyxdb.mdb.core.clusters.models.ClusterHost;
import com.onyxdb.mdb.core.clusters.repositories.ClusterHostRepository;

public class ClusterHostService {
    private final ClusterHostRepository clusterHostRepository;

    public ClusterHostService(ClusterHostRepository clusterHostRepository) {
        this.clusterHostRepository = clusterHostRepository;
    }

    public void upsertHosts(List<ClusterHost> hosts) {
        clusterHostRepository.createHosts(hosts);
    }
}
