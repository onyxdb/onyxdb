package com.onyxdb.mdb.quotas;

import java.util.UUID;

public record Quota(
        UUID resourceId,
        long limit,
        long usage,
        long free
) {
}
