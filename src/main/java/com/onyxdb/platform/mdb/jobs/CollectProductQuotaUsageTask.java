package com.onyxdb.platform.mdb.jobs;

import java.util.List;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import com.onyxdb.platform.mdb.billing.BillingService;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.quotas.EnrichedProductQuota;
import com.onyxdb.platform.mdb.quotas.QuotaFilter;
import com.onyxdb.platform.mdb.quotas.QuotaService;

public class CollectProductQuotaUsageTask {
    private static final Logger logger = LoggerFactory.getLogger(CollectProductQuotaUsageTask.class);

    private final BillingService billingService;
    private final ClusterService clusterService;
    private final QuotaService quotaService;

    public CollectProductQuotaUsageTask(
            BillingService billingService,
            ClusterService clusterService,
            QuotaService quotaService
    ) {
        this.billingService = billingService;
        this.clusterService = clusterService;
        this.quotaService = quotaService;
    }

    @Scheduled(fixedRateString = "${onyxdb.tasks.collect-product-quota-usage.fixed-rate}")
    @SchedulerLock(
            name = "CollectProductQuotaUsageTask",
            lockAtLeastFor = "${onyxdb.tasks.collect-product-quota-usage.lock-at-least-for}",
            lockAtMostFor = "${onyxdb.tasks.collect-product-quota-usage.lock-at-most-for}"
    )
    public void scheduledTask() {
        int collectedQuotas = 0;
        logger.debug("Started collecting product quota usage");
        try {
            List<EnrichedProductQuota> productQuotas = quotaService.listProductQuotas(QuotaFilter.builder().build());
            collectedQuotas = productQuotas.size();
            billingService.saveProductQuotaUsageRecords(productQuotas);
        } catch (Exception e) {
            logger.error("Failed to collect product quota usage", e);
            return;
        }
        logger.debug("Finished collecting product quota usage. Collected {} records", collectedQuotas);
    }
}
