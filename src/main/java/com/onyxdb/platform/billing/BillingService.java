package com.onyxdb.platform.billing;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class BillingService {
    private final BillingRepository billingRepository;

    public BillingService(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    public List<UsageReportItem> getUsageReportByProduct(
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
}
