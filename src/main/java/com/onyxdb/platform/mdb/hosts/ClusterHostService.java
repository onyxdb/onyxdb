package com.onyxdb.platform.mdb.hosts;

import java.util.List;

import com.onyxdb.platform.mdb.clusters.models.ClusterHost;

public class ClusterHostService {
    private final ClusterHostRepository clusterHostRepository;

    public ClusterHostService(ClusterHostRepository clusterHostRepository) {
        this.clusterHostRepository = clusterHostRepository;
    }

    public void upsertHosts(List<ClusterHost> hosts) {
        clusterHostRepository.createHosts(hosts);
    }
}
