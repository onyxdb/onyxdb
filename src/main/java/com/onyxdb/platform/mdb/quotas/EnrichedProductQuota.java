package com.onyxdb.platform.mdb.quotas;

import java.util.UUID;

import com.onyxdb.platform.mdb.resources.Resource;

public record EnrichedProductQuota(
        UUID productId,
        Resource resource,
        long limit,
        long usage,
        long free
) {
    public EnrichedProductQuota subtract(QuotaToTransfer q) {
        return new EnrichedProductQuota(
                productId,
                resource,
                limit - q.limit(),
                usage,
                free - q.limit()
        );
    }

    public EnrichedProductQuota add(QuotaToTransfer q) {
        return new EnrichedProductQuota(
                productId,
                resource,
                limit + q.limit(),
                usage,
                free + q.limit()
        );
    }

    public EnrichedProductQuota subtractUsage(long usage) {
        return new EnrichedProductQuota(
                productId,
                resource,
                this.limit,
                this.usage - usage,
                this.free + usage
        );
    }

    public EnrichedProductQuota addUsage(long usage) {
        return new EnrichedProductQuota(
                productId,
                resource,
                this.limit,
                this.usage + usage,
                this.free - usage
        );
    }

    public static EnrichedProductQuota empty(UUID productId, Resource resource) {
        return new EnrichedProductQuota(
                productId,
                resource,
                0,
                0,
                0
        );
    }
}
