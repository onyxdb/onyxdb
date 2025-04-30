package com.onyxdb.platform.mdb.billing;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.onyxdb.platform.generated.openapi.models.GetProductQuotaUsageReportResponseOA;
import com.onyxdb.platform.generated.openapi.models.ProductQuotaUsageByResourceOA;
import com.onyxdb.platform.generated.openapi.models.ProductQuotaUsageReportItemOA;
import com.onyxdb.platform.mdb.quotas.QuotaProvider;
import com.onyxdb.platform.mdb.resources.Resource;
import com.onyxdb.platform.mdb.resources.ResourceMapper;

public class BillingMapper {
    private static final Logger logger = LoggerFactory.getLogger(BillingMapper.class);

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

    public GetProductQuotaUsageReportResponseOA toGetProductQuotaUsageReportResponse(
            List<Resource> resources,
            List<ProductQuotaUsageReportItem> items,
            ResourceMapper resourceMapper
    ) {
        Map<UUID, Resource> resourceIdToResource = resources.stream()
                .collect(Collectors.toMap(Resource::id, r -> r));

        Map<UUID, List<ProductQuotaUsageReportItem>> resourceIdToReportItems = items.stream()
                .collect(Collectors.groupingBy(ProductQuotaUsageReportItem::resourceId, Collectors.toList()));

        List<ProductQuotaUsageByResourceOA> productQuotaUsageByResourceOAList = new ArrayList<>();

        resourceIdToReportItems.forEach((resourceId, value) -> {
            if (!resourceIdToResource.containsKey(resourceId)) {
                logger.warn("Can't find resource with id '{}'", resourceId);
                return;
            }

            productQuotaUsageByResourceOAList.add(
                    new ProductQuotaUsageByResourceOA(
                            resourceMapper.map(resourceIdToResource.get(resourceId)),
                            value.stream().map(this::toProductQuotaUsageReportItemOA).toList()
                    )
            );
        });

        return new GetProductQuotaUsageReportResponseOA(
                productQuotaUsageByResourceOAList
        );
    }
}
