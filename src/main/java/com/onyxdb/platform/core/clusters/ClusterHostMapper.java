package com.onyxdb.platform.core.clusters;

import com.onyxdb.platform.core.clusters.models.ClusterHost;
import com.onyxdb.platform.generated.jooq.tables.records.ClusterHostsRecord;

public class ClusterHostMapper {
    public ClusterHostsRecord toClusterHostsRecord(ClusterHost h) {
        return new ClusterHostsRecord(
                h.name(),
                h.clusterId()
        );
    }
}
