package com.onyxdb.platform.core.clusters.models;

import java.util.UUID;

// TODO delete and use Host
public record ClusterHost(
        String name,
        UUID clusterId
) {
}
