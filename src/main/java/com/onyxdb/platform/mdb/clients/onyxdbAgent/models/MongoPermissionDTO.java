package com.onyxdb.platform.mdb.clients.onyxdbAgent.models;

import java.util.List;

public record MongoPermissionDTO(
        String database,
        List<String> roles
) {
}
