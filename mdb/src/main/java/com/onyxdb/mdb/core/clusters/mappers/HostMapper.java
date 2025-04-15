package com.onyxdb.mdb.core.clusters.mappers;

import java.util.List;

import com.onyxdb.mdb.core.clusters.models.EnrichedHost;
import com.onyxdb.mdb.core.clusters.models.Host;
import com.onyxdb.mdb.core.clusters.models.MongoHost;
import com.onyxdb.mdb.core.clusters.models.MongoHostRole;
import com.onyxdb.mdb.core.clusters.models.MongoHostStatus;
import com.onyxdb.mdb.core.clusters.models.MongoHostType;
import com.onyxdb.mdb.generated.jooq.tables.records.ClusterHostsRecord;
import com.onyxdb.mdb.generated.openapi.models.UpdateMongoHostsRequest;

public class HostMapper {
    public com.onyxdb.mdb.generated.openapi.models.MongoHost map(MongoHost h) {
        return new com.onyxdb.mdb.generated.openapi.models.MongoHost(
                h.name(),
                h.clusterId(),
                com.onyxdb.mdb.generated.openapi.models.MongoHostType.fromValue(h.type().value()),
                com.onyxdb.mdb.generated.openapi.models.MongoHostStatus.fromValue(h.status().value()),
                com.onyxdb.mdb.generated.openapi.models.MongoHostRole.fromValue(h.role().value())
        );
    }

    public List<MongoHost> map(UpdateMongoHostsRequest rq) {
        return rq.getHosts()
                .stream()
                .map(this::map)
                .toList();
    }

    public MongoHost map(com.onyxdb.mdb.generated.openapi.models.MongoHost s) {
        return new MongoHost(
                s.getName(),
                s.getClusterId(),
                MongoHostType.R.fromValueOrDefault(s.getType().getValue(), MongoHostType.UNKNOWN),
                MongoHostStatus.R.fromValueOrDefault(s.getStatus().getValue(), MongoHostStatus.UNKNOWN),
                MongoHostRole.R.fromValueOrDefault(s.getRole().getValue(), MongoHostRole.UNKNOWN)
        );
    }

    public ClusterHostsRecord map(Host h) {
        return new ClusterHostsRecord(
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
}
