package com.onyxdb.mdb.core.clusters.repositories;

import java.util.List;
import java.util.UUID;

import com.onyxdb.mdb.core.clusters.models.EnrichedHost;

public interface EnrichedHostRepository {
    List<EnrichedHost> listEnrichedHosts(UUID clusterId, List<String> hosts);

    void upsertEnrichedHosts(List<EnrichedHost> hosts);
}
