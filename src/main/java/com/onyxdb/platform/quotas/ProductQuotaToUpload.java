package com.onyxdb.platform.quotas;

import java.util.UUID;

public record ProductQuotaToUpload(
        UUID productId,
        UUID resourceId,
        long limit
) {
}
