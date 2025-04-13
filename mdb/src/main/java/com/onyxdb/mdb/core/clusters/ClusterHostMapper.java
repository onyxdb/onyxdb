package com.onyxdb.mdb.core.clusters;

import com.onyxdb.mdb.core.clusters.models.ClusterHost;
import com.onyxdb.mdb.generated.jooq.tables.records.ClusterHostsRecord;

public class ClusterHostMapper {
    public ClusterHostsRecord toClusterHostsRecord(ClusterHost h) {
        return new ClusterHostsRecord(
                h.name(),
                h.clusterId()
        );
    }
}
