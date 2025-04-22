package com.onyxdb.platform.core.clusters.models;

import java.util.UUID;

public record MongoHost(
        String name,
        UUID clusterId,
        MongoHostType type,
        MongoHostStatus status,
        MongoHostRole role
) {
}
