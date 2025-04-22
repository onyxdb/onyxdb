package com.onyxdb.platform.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.platform.billing.BillingService;
import com.onyxdb.platform.core.clusters.ClusterService;
import com.onyxdb.platform.quotas.QuotaService;
import com.onyxdb.platform.tasks.CollectProductQuotaUsageTask;

@Configuration
public class TasksConfig {
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
