package com.onyxdb.platform.billing;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BillingRepository {
    List<UsageReportItem> getUsageReportByProduct(
            UUID productId,
            LocalDate starDate,
            LocalDate endDate
    );
}
