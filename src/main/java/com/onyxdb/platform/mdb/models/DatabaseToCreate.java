package com.onyxdb.platform.mdb.models;

import java.util.UUID;

public record DatabaseToCreate(
        UUID clusterId,
        String databaseName,
        UUID createdBy
) {
}
