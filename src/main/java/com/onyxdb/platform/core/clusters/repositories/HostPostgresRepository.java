package com.onyxdb.platform.core.clusters.repositories;

import java.util.List;
import java.util.UUID;

import org.jooq.DSLContext;

import com.onyxdb.platform.core.clusters.mappers.HostMapper;
import com.onyxdb.platform.core.clusters.models.Host;
import com.onyxdb.platform.generated.jooq.tables.records.ClusterHostsRecord;

import static com.onyxdb.platform.generated.jooq.Tables.CLUSTER_HOSTS;

public class HostPostgresRepository implements HostRepository {
    private final DSLContext dslContext;
    private final HostMapper hostMapper;

    public HostPostgresRepository(
            DSLContext dslContext,
            HostMapper hostMapper
    ) {
        this.dslContext = dslContext;
        this.hostMapper = hostMapper;
    }

    @Override
    public void createHosts(List<Host> hosts) {
        List<ClusterHostsRecord> records = hosts.stream()
                .map(hostMapper::map)
                .toList();

        dslContext.batchInsert(records).execute();
    }

    @Override
    public List<Host> listHosts(UUID clusterId) {
        return dslContext.select(
                        CLUSTER_HOSTS.NAME,
                        CLUSTER_HOSTS.CLUSTER_ID
                )
                .from(CLUSTER_HOSTS)
                .where(CLUSTER_HOSTS.CLUSTER_ID.eq(clusterId))
                .fetchInto(Host.class);
    }
}
