package com.onyxdb.platform.mdb.clusters.models;

import java.util.UUID;

public record Host(
        String name,
        UUID clusterId
) {
}
