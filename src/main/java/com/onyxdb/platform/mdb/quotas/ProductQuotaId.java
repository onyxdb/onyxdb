package com.onyxdb.platform.mdb.quotas;

import java.util.UUID;

public record ProductQuotaId(
        UUID productId,
        UUID resourceId
) {
}
