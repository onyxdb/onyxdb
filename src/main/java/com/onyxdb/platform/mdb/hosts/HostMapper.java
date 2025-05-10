package com.onyxdb.platform.mdb.hosts;

import java.util.List;
import java.util.UUID;

import com.onyxdb.platform.generated.jooq.tables.records.HostsRecord;
import com.onyxdb.platform.generated.openapi.models.MongoHostDTO;
import com.onyxdb.platform.generated.openapi.models.UpdateMongoHostsRequestDTO;
import com.onyxdb.platform.mdb.clusters.models.EnrichedHost;
import com.onyxdb.platform.mdb.clusters.models.Host;
import com.onyxdb.platform.mdb.clusters.models.MongoHost;
import com.onyxdb.platform.mdb.clusters.models.MongoHostRole;
import com.onyxdb.platform.mdb.clusters.models.MongoHostStatus;
import com.onyxdb.platform.mdb.clusters.models.MongoHostType;

public class HostMapper {
    public MongoHostDTO map(MongoHost h) {
        return new MongoHostDTO(
                h.name(),
                h.clusterId(),
                h.type().value(),
                h.status().value(),
                h.role().value()
        );
    }

    public List<MongoHost> map(UpdateMongoHostsRequestDTO rq) {
        return rq.getHosts()
                .stream()
                .map(this::map)
                .toList();
    }

    public MongoHost map(MongoHostDTO s) {
        return new MongoHost(
                s.getName(),
                s.getClusterId(),
                MongoHostType.R.fromValueOrDefault(s.getType(), MongoHostType.UNKNOWN),
                MongoHostStatus.R.fromValueOrDefault(s.getStatus(), MongoHostStatus.UNKNOWN),
                MongoHostRole.R.fromValueOrDefault(s.getRole(), MongoHostRole.UNKNOWN)
        );
    }

    public HostsRecord map(Host h) {
        return new HostsRecord(
                h.name(),
                h.clusterId()
        );
    }

    public MongoHost map(EnrichedHost h) {
        return new MongoHost(
                h.name(),
                h.clusterId(),
                MongoHostType.R.fromValueOrDefault(h.type(), MongoHostType.UNKNOWN),
                MongoHostStatus.R.fromValueOrDefault(h.status(), MongoHostStatus.UNKNOWN),
                MongoHostRole.R.fromValueOrDefault(h.role(), MongoHostRole.UNKNOWN)
        );
    }

    public EnrichedHost mapToEnrichedHost(MongoHost h) {
        return new EnrichedHost(
                h.name(),
                h.clusterId(),
                h.type().value(),
                h.status().value(),
                h.role().value()
        );
    }

    public Host hostNameToHost(String hostName, UUID clusterId) {
        return new Host(
                hostName,
                clusterId
        );
    }

    public List<Host> hostNamesToHosts(List<String> hostNames, UUID clusterId) {
        return hostNames.stream().map(h -> hostNameToHost(h, clusterId)).toList();
    }
}
