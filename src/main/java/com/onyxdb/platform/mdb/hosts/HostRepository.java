package com.onyxdb.platform.mdb.hosts;

import java.util.List;
import java.util.UUID;

import com.onyxdb.platform.mdb.clusters.models.Host;

public interface HostRepository {
    void upsertHosts(List<Host> hosts);

    List<Host> listHosts(UUID clusterId);

    void deleteNotMatchingHosts(UUID id, List<String> hostnames);
}
