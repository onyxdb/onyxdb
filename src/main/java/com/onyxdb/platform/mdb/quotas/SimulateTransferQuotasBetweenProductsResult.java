package com.onyxdb.platform.mdb.quotas;

import java.util.List;

public record SimulateTransferQuotasBetweenProductsResult(
        List<EnrichedProductQuota> srcQuotas,
        List<EnrichedProductQuota> dstQuotas
) {
}
