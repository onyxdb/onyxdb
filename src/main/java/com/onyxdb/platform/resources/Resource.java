package com.onyxdb.platform.resources;

import java.util.UUID;

import com.onyxdb.platform.quotas.QuotaProvider;

public record Resource(
        UUID id,
        String name,
        String description,
        ResourceType type,
        QuotaProvider provider
) {
}
