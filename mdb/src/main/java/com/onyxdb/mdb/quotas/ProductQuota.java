package com.onyxdb.mdb.quotas;

import java.util.UUID;

public record ProductQuota(
        UUID productId,
        UUID resourceId,
        long limit,
        long usage,
        long free
) {
    public ProductQuota subtract(QuotaToTransfer q) {
        return new ProductQuota(
                productId,
                resourceId,
                limit - q.limit(),
                usage,
                free - q.limit()
        );
    }

    public ProductQuota add(QuotaToTransfer q) {
        return new ProductQuota(
                productId,
                resourceId,
                limit + q.limit(),
                usage,
                free + q.limit()
        );
    }

    public static ProductQuota empty(UUID productId, UUID resourceId) {
        return new ProductQuota(
                productId,
                resourceId,
                0,
                0,
                0
        );
    }
}
