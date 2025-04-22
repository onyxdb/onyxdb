package com.onyxdb.platform.core.clusters.models;

import java.util.List;

public record PermissionData(
        List<MongoRole> roles
) {
}
