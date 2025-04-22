package com.onyxdb.platform.quotas;

import java.util.UUID;

public record Quota(
        UUID resourceId,
        long limit,
        long usage,
        long free
) {
}
