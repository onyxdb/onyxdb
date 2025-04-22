package com.onyxdb.mdb.quotas;

import java.util.UUID;

public record ProductQuotaId(
        UUID productId,
        UUID resourceId
) {
}
