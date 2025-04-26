package com.onyxdb.platform.mdb.hosts;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.onyxdb.platform.core.clusters.models.EnrichedHost;
import com.onyxdb.platform.core.clusters.models.Host;
import com.onyxdb.platform.core.clusters.models.MongoHost;

public class HostService {
    private final HostRepository hostRepository;
    private final EnrichedHostRepository enrichedHostRepository;
    private final HostMapper hostMapper;

    public HostService(
            HostRepository hostRepository,
            EnrichedHostRepository enrichedHostRepository,
            HostMapper hostMapper
    ) {
        this.hostRepository = hostRepository;
        this.enrichedHostRepository = enrichedHostRepository;
        this.hostMapper = hostMapper;
    }

    public List<MongoHost> listMongoHosts(UUID clusterId) {
        return listEnrichedHosts(clusterId).stream().map(hostMapper::map).toList();
    }

    public List<EnrichedHost> listEnrichedHosts(UUID clusterId) {
        List<Host> hosts = hostRepository.listHosts(clusterId);
        List<String> hostNames = hosts.stream().map(Host::name).toList();

        List<EnrichedHost> enrichedHosts = enrichedHostRepository.listEnrichedHosts(clusterId, hostNames);
        Map<String, EnrichedHost> hostToEnrichedHost = enrichedHosts.stream()
                .collect(Collectors.toMap(EnrichedHost::name, h -> h));

        return hosts.stream()
                .map(h -> Objects.requireNonNullElseGet(
                        hostToEnrichedHost.get(h.name()),
                        () -> EnrichedHost.withoutMeta(h.name(), h.clusterId())
                ))
                .toList();
    }

    public void upsertHosts(List<Host> hosts) {
        hostRepository.upsertHosts(hosts);
    }

    public void updateMongoHosts(List<MongoHost> hosts) {
        List<EnrichedHost> enrichedHosts = hosts.stream().map(hostMapper::mapToEnrichedHost).toList();
        enrichedHostRepository.upsertEnrichedHosts(enrichedHosts);
    }
}
