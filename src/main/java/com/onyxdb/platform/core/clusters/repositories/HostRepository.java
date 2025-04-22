package com.onyxdb.platform.core.clusters.repositories;

import java.util.List;
import java.util.UUID;

import com.onyxdb.platform.core.clusters.models.Host;

public interface HostRepository {
    void createHosts(List<Host> hosts);

    List<Host> listHosts(UUID clusterId);
}
