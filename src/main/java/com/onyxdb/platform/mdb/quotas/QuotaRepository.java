package com.onyxdb.platform.mdb.quotas;

import java.util.List;
import java.util.UUID;

public interface QuotaRepository {
    List<EnrichedProductQuota> listProductQuotas(QuotaFilter filter);

    void addProductQuotas(List<ProductQuotaToUpload> quotas);

    void subtractProductQuotas(List<ProductQuotaToUpload> quotas);

    void addProductQuotas(UUID toProductId, List<QuotaToTransfer> quotas);

    void subtractProductQuotas(UUID fromProductId, List<QuotaToTransfer> quotas);
}
