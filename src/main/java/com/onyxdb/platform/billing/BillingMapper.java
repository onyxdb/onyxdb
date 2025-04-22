package com.onyxdb.platform.billing;

import java.sql.Timestamp;

import com.onyxdb.platform.generated.openapi.models.ProductQuotaUsageReportItemOA;
import com.onyxdb.platform.quotas.QuotaProvider;

public class BillingMapper {
    public ProductQuotaUsageReportItemOA toProductQuotaUsageReportItemOA(ProductQuotaUsageReportItem i) {
        return new ProductQuotaUsageReportItemOA(
                i.productId(),
                QuotaProvider.MDB.value(),
                i.limit(),
                i.usage(),
                i.free(),
                Timestamp.valueOf(i.date().atStartOfDay()).getTime()
        );
    }
}
