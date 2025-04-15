package com.onyxdb.mdb.core.clusters.repositories;

import java.util.List;
import java.util.UUID;

import com.onyxdb.mdb.core.clusters.models.Host;

public interface HostRepository {
    void createHosts(List<Host> hosts);

    List<Host> listHosts(UUID clusterId);
}
