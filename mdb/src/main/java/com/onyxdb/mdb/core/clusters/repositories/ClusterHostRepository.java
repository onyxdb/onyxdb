package com.onyxdb.mdb.core.clusters.repositories;

import java.util.List;

import com.onyxdb.mdb.core.clusters.models.ClusterHost;

public interface ClusterHostRepository {
    void createHosts(List<ClusterHost> hosts);
}
