package com.onyxdb.platform.mdb.resources;

import java.util.UUID;

import com.onyxdb.platform.mdb.quotas.QuotaProvider;

public record Resource(
        UUID id,
        String name,
        String description,
        ResourceType type,
        QuotaProvider provider
) {
}
