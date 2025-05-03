package com.onyxdb.platform.mdb.hosts;

import java.util.List;
import java.util.UUID;

import com.onyxdb.platform.generated.jooq.tables.records.HostsRecord;
import com.onyxdb.platform.generated.openapi.models.UpdateMongoHostsRequest;
import com.onyxdb.platform.mdb.models.EnrichedHost;
import com.onyxdb.platform.mdb.models.Host;
import com.onyxdb.platform.mdb.models.MongoHost;
import com.onyxdb.platform.mdb.models.MongoHostRole;
import com.onyxdb.platform.mdb.models.MongoHostStatus;
import com.onyxdb.platform.mdb.models.MongoHostType;

public class HostMapper {
    public com.onyxdb.platform.generated.openapi.models.MongoHost map(MongoHost h) {
        return new com.onyxdb.platform.generated.openapi.models.MongoHost(
                h.name(),
                h.clusterId(),
                com.onyxdb.platform.generated.openapi.models.MongoHostType.fromValue(h.type().value()),
                com.onyxdb.platform.generated.openapi.models.MongoHostStatus.fromValue(h.status().value()),
                com.onyxdb.platform.generated.openapi.models.MongoHostRole.fromValue(h.role().value())
        );
    }

    public List<MongoHost> map(UpdateMongoHostsRequest rq) {
        return rq.getHosts()
                .stream()
                .map(this::map)
                .toList();
    }

    public MongoHost map(com.onyxdb.platform.generated.openapi.models.MongoHost s) {
        return new MongoHost(
                s.getName(),
                s.getClusterId(),
                MongoHostType.R.fromValueOrDefault(s.getType().getValue(), MongoHostType.UNKNOWN),
                MongoHostStatus.R.fromValueOrDefault(s.getStatus().getValue(), MongoHostStatus.UNKNOWN),
                MongoHostRole.R.fromValueOrDefault(s.getRole().getValue(), MongoHostRole.UNKNOWN)
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
