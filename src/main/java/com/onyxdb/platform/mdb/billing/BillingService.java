package com.onyxdb.platform.mdb.billing;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.onyxdb.platform.mdb.quotas.EnrichedProductQuota;

public class BillingService {
    private final BillingRepository billingRepository;

    public BillingService(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    public List<ProductQuotaUsageReportItem> getUsageReportByProduct(
            UUID productId,
            LocalDate starDate,
            LocalDate endDate
    ) {
        return billingRepository.getUsageReportByProduct(
                productId,
                starDate,
                endDate
        );
    }

    public void saveProductQuotaUsageRecords(List<EnrichedProductQuota> quotas) {
        billingRepository.saveProductQuotaUsageRecords(quotas);
    }
}
