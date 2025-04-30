package com.onyxdb.platform.mdb.hosts;

import com.onyxdb.platform.generated.jooq.tables.records.ClusterHostsRecord;
import com.onyxdb.platform.mdb.models.ClusterHost;

public class ClusterHostMapper {
    public ClusterHostsRecord toClusterHostsRecord(ClusterHost h) {
        return new ClusterHostsRecord(
                h.name(),
                h.clusterId()
        );
    }
}
