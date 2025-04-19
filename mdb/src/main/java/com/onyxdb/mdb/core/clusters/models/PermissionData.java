package com.onyxdb.mdb.core.clusters.models;

import java.util.List;

public record PermissionData(
        List<MongoRole> roles
) {
}
