package com.onyxdb.platform.mdb.clusters.models;

import java.util.List;

public record PermissionData(
        List<MongoRole> roles
) {
}
