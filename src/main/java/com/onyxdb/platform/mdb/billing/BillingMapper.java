package com.onyxdb.platform.mdb.billing;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.onyxdb.platform.generated.openapi.models.GetProductQuotaUsageReportResponseDTO;
import com.onyxdb.platform.generated.openapi.models.ProductQuotaUsageByResourceDTO;
import com.onyxdb.platform.generated.openapi.models.ProductQuotaUsageReportItemDTO;
import com.onyxdb.platform.mdb.quotas.QuotaProvider;
import com.onyxdb.platform.mdb.resources.Resource;
import com.onyxdb.platform.mdb.resources.ResourceMapper;

public class BillingMapper {
    private static final Logger logger = LoggerFactory.getLogger(BillingMapper.class);

    public ProductQuotaUsageReportItemDTO toProductQuotaUsageReportItemOA(ProductQuotaUsageReportItem i) {
        return new ProductQuotaUsageReportItemDTO(
                i.productId(),
                QuotaProvider.MDB.value(),
                i.limit(),
                i.usage(),
                i.free(),
                Timestamp.valueOf(i.date().atStartOfDay()).getTime()
        );
    }

    public GetProductQuotaUsageReportResponseDTO toGetProductQuotaUsageReportResponse(
            List<Resource> resources,
            List<ProductQuotaUsageReportItem> items,
            ResourceMapper resourceMapper
    ) {
        Map<UUID, Resource> resourceIdToResource = resources.stream()
                .collect(Collectors.toMap(Resource::id, r -> r));

        Map<UUID, List<ProductQuotaUsageReportItem>> resourceIdToReportItems = items.stream()
                .collect(Collectors.groupingBy(ProductQuotaUsageReportItem::resourceId, Collectors.toList()));

        List<ProductQuotaUsageByResourceDTO> productQuotaUsageByResourceOAList = new ArrayList<>();

        resourceIdToReportItems.forEach((resourceId, value) -> {
            if (!resourceIdToResource.containsKey(resourceId)) {
                logger.warn("Can't find resource with id '{}'", resourceId);
                return;
            }

            productQuotaUsageByResourceOAList.add(
                    new ProductQuotaUsageByResourceDTO(
                            resourceMapper.resourceToResourceDTO(resourceIdToResource.get(resourceId)),
                            value.stream().map(this::toProductQuotaUsageReportItemOA).toList()
                    )
            );
        });

        return new GetProductQuotaUsageReportResponseDTO(
                productQuotaUsageByResourceOAList
        );
    }
}
