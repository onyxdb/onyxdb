package com.onyxdb.platform.mdb.models;

import java.util.UUID;

public record CreateDatabase(
        String name,
        UUID clusterId,
        UUID createdBy
) {
}
