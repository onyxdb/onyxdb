package com.onyxdb.platform.billing;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.onyxdb.platform.quotas.EnrichedProductQuota;

public interface BillingRepository {
    List<ProductQuotaUsageReportItem> getUsageReportByProduct(
            UUID productId,
            LocalDate starDate,
            LocalDate endDate
    );

    void saveProductQuotaUsageRecords(List<EnrichedProductQuota> quotas);
}
