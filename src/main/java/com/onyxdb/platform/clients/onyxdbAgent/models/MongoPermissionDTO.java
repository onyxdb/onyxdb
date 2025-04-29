package com.onyxdb.platform.clients.onyxdbAgent.models;

import java.util.List;

public record MongoPermissionDTO(
        String database,
        List<String> roles
) {
}
