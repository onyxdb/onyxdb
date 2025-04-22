package com.onyxdb.platform.core.clusters.models;

import java.util.UUID;

public record Host(
        String name,
        UUID clusterId
) {
}
