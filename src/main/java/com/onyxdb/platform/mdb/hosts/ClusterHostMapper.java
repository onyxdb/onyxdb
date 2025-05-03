package com.onyxdb.platform.mdb.hosts;

import com.onyxdb.platform.generated.jooq.tables.records.HostsRecord;
import com.onyxdb.platform.mdb.models.ClusterHost;

public class ClusterHostMapper {
    public HostsRecord toClusterHostsRecord(ClusterHost h) {
        return new HostsRecord(
                h.name(),
                h.clusterId()
        );
    }
}
