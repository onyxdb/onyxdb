package com.onyxdb.platform.mdb.models;

import java.util.UUID;

// TODO delete and use Host
public record ClusterHost(
        String name,
        UUID clusterId
) {
}
