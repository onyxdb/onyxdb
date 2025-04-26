package com.onyxdb.platform.clients.onyxdbAgent.models;

import java.util.List;

import com.onyxdb.platform.core.clusters.models.MongoRole;

public record MongoPermissionDTO(
        String database,
        List<String> roles
) {
}
