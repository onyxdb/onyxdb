package com.onyxdb.platform.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.platform.billing.BillingService;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.quotas.QuotaService;
import com.onyxdb.platform.tasks.CollectProductQuotaUsageTask;

@Configuration
public class TasksContextConfiguration {
    @Bean
    public CollectProductQuotaUsageTask collectQuotaUsageTask(
            BillingService billingService,
            ClusterService clusterService,
            QuotaService quotaService
    ) {
        return new CollectProductQuotaUsageTask(
                billingService,
                clusterService,
                quotaService
        );
    }
}
