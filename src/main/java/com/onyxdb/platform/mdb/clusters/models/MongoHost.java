package com.onyxdb.platform.mdb.clusters.models;

import java.util.UUID;

public record MongoHost(
        String name,
        UUID clusterId,
        MongoHostType type,
        MongoHostStatus status,
        MongoHostRole role
) {
}
