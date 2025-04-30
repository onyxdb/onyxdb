package com.onyxdb.platform.mdb.models;

import java.util.List;

public record PermissionData(
        List<MongoRole> roles
) {
}
