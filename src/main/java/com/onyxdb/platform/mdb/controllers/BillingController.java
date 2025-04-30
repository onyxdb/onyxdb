package com.onyxdb.platform.mdb.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.BillingApi;
import com.onyxdb.platform.generated.openapi.models.GetProductQuotaUsageReportResponseOA;
import com.onyxdb.platform.mdb.billing.BillingMapper;
import com.onyxdb.platform.mdb.billing.BillingService;
import com.onyxdb.platform.mdb.billing.ProductQuotaUsageReportItem;
import com.onyxdb.platform.mdb.resources.Resource;
import com.onyxdb.platform.mdb.resources.ResourceFilter;
import com.onyxdb.platform.mdb.resources.ResourceMapper;
import com.onyxdb.platform.mdb.resources.ResourceService;

@RestController
public class BillingController implements BillingApi {
    private final BillingService billingService;
    private final BillingMapper billingMapper;
    private final ResourceService resourceService;
    private final ResourceMapper resourceMapper;

    public BillingController(
            BillingService billingService,
            BillingMapper billingMapper,
            ResourceService resourceService,
            ResourceMapper resourceMapper
    ) {
        this.billingService = billingService;
        this.billingMapper = billingMapper;
        this.resourceService = resourceService;
        this.resourceMapper = resourceMapper;
    }

    @Override
    public ResponseEntity<GetProductQuotaUsageReportResponseOA> getProductQuotaUsageReport(
            UUID productId,
            LocalDate starDate,
            LocalDate endDate
    ) {
        List<ProductQuotaUsageReportItem> reportItems = billingService.getUsageReportByProduct(
                productId,
                starDate,
                endDate
        );
        List<Resource> resources = resourceService.listResources(ResourceFilter.builder().build());


        GetProductQuotaUsageReportResponseOA response = billingMapper.toGetProductQuotaUsageReportResponse(
                resources,
                reportItems,
                resourceMapper
        );
        return ResponseEntity.ok(response);
    }
}
