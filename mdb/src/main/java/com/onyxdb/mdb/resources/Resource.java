package com.onyxdb.mdb.resources;

import java.util.UUID;

import com.onyxdb.mdb.quotas.QuotaProvider;

public record Resource(
        UUID id,
        String name,
        String description,
        ResourceType type,
        QuotaProvider provider
) {
}
