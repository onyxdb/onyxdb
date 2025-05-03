package com.onyxdb.platform.mdb.hosts;

import java.util.List;

import com.onyxdb.platform.mdb.clusters.models.ClusterHost;

public interface ClusterHostRepository {
    void createHosts(List<ClusterHost> hosts);
}
