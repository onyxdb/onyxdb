package com.onyxdb.platform.mdb.hosts;

import java.util.List;

import org.jooq.DSLContext;

import com.onyxdb.platform.core.clusters.models.ClusterHost;
import com.onyxdb.platform.generated.jooq.tables.records.ClusterHostsRecord;

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
        List<ClusterHostsRecord> records = hosts.stream()
                .map(clusterHostMapper::toClusterHostsRecord)
                .toList();

        dslContext.batchInsert(records).execute();
    }
}
