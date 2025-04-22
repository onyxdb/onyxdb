package com.onyxdb.platform.billing;

import java.time.LocalDate;
import java.util.UUID;

public record UsageReportItem(
        UUID productId,
        long limit,
        long usage,
        long free,
        LocalDate date
) {
}
