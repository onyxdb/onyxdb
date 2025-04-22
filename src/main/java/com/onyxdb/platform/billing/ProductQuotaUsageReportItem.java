package com.onyxdb.platform.billing;

import java.time.LocalDate;
import java.util.UUID;

public record ProductQuotaUsageReportItem(
        UUID productId,
        UUID resourceId,
        long limit,
        long usage,
        long free,
        LocalDate date
) {
}
