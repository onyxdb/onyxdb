package com.onyxdb.platform.mdb.hosts;

import java.util.List;
import java.util.UUID;

import com.onyxdb.platform.mdb.models.EnrichedHost;

public interface EnrichedHostRepository {
    List<EnrichedHost> listEnrichedHosts(UUID clusterId, List<String> hosts);

    void upsertEnrichedHosts(List<EnrichedHost> hosts);
}
