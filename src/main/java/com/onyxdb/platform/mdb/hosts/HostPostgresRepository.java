package com.onyxdb.platform.mdb.hosts;

import java.util.List;
import java.util.UUID;

import org.jooq.DSLContext;

import com.onyxdb.platform.generated.jooq.tables.records.HostsRecord;
import com.onyxdb.platform.mdb.clusters.models.Host;

import static com.onyxdb.platform.generated.jooq.Tables.HOSTS;

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
    public void upsertHosts(List<Host> hosts) {
        List<HostsRecord> records = hosts.stream()
                .map(hostMapper::map)
                .toList();

        dslContext.insertInto(HOSTS)
                .set(records)
                .onConflict()
                .doNothing()
                .execute();
    }

    @Override
    public List<Host> listHosts(UUID clusterId) {
        return dslContext.select(
                        HOSTS.NAME,
                        HOSTS.CLUSTER_ID
                )
                .from(HOSTS)
//                .where(HOSTS.CLUSTER_ID.eq(id))
                .fetchInto(Host.class);
    }

    @Override
    public void deleteNotMatchingHosts(UUID id, List<String> hostnames) {
        dslContext.deleteFrom(HOSTS)
                .where(HOSTS.CLUSTER_ID.eq(id).and(HOSTS.NAME.notIn(hostnames)))
                .execute();
    }
}
