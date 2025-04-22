package com.onyxdb.platform.core.clusters.repositories;

import java.util.List;

import com.onyxdb.platform.core.clusters.models.ClusterHost;

public interface ClusterHostRepository {
    void createHosts(List<ClusterHost> hosts);
}
