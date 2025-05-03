package com.onyxdb.platform.mdb.hosts;

import java.util.List;

import org.jooq.DSLContext;

import com.onyxdb.platform.generated.jooq.tables.records.HostsRecord;
import com.onyxdb.platform.mdb.clusters.models.ClusterHost;

public class ClusterHostPostgresRepository implements ClusterHostRepository {
    private final DSLContext dslContext;
    private final ClusterHostMapper clusterHostMapper;

    public ClusterHostPostgresRepository(
            DSLContext dslContext,
            ClusterHostMapper clusterHostMapper
    ) {
        this.dslContext = dslContext;
        this.clusterHostMapper = clusterHostMapper;
    }

    @Override
    public void createHosts(List<ClusterHost> hosts) {
        List<HostsRecord> records = hosts.stream()
                .map(clusterHostMapper::toClusterHostsRecord)
                .toList();

        dslContext.batchInsert(records).execute();
    }
}
