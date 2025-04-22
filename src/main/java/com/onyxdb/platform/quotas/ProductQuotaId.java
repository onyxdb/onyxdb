package com.onyxdb.platform.quotas;

import java.util.UUID;

public record ProductQuotaId(
        UUID productId,
        UUID resourceId
) {
}
