package com.onyxdb.mdb.core.clusters.models;

import java.util.UUID;

public record ClusterHost(
        String name,
        UUID clusterId
) {
}
