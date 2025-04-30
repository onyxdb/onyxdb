package com.onyxdb.platform.mdb.hosts;

import java.util.List;

import com.onyxdb.platform.mdb.models.ClusterHost;

public interface ClusterHostRepository {
    void createHosts(List<ClusterHost> hosts);
}
