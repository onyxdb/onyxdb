package com.onyxdb.platform.mdb.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.platform.mdb.billing.BillingService;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.jobs.CollectProductQuotaUsageTask;
import com.onyxdb.platform.mdb.quotas.QuotaService;

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
