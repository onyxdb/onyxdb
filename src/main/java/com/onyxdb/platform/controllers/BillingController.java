package com.onyxdb.platform.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.billing.BillingMapper;
import com.onyxdb.platform.billing.BillingService;
import com.onyxdb.platform.billing.UsageReportItem;
import com.onyxdb.platform.generated.openapi.apis.BillingApi;
import com.onyxdb.platform.generated.openapi.models.GetProductQuotaUsageReportResponseOA;

@RestController
public class BillingController implements BillingApi {
    private final BillingService billingService;
    private final BillingMapper billingMapper;

    public BillingController(
            BillingService billingService,
            BillingMapper billingMapper
    ) {
        this.billingService = billingService;
        this.billingMapper = billingMapper;
    }

    @Override
    public ResponseEntity<GetProductQuotaUsageReportResponseOA> getProductQuotaUsageReport(
            UUID productId,
            LocalDate starDate,
            LocalDate endDate
    ) {
        List<UsageReportItem> reportItems = billingService.getUsageReportByProduct(
                productId,
                starDate,
                endDate
        );

        var response = new GetProductQuotaUsageReportResponseOA(
                reportItems.stream().map(billingMapper::toProductQuotaUsageReportItemOA).toList()
        );
        return ResponseEntity.ok(response);
    }
}
