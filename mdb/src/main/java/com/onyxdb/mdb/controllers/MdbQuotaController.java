package com.onyxdb.mdb.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.mdb.generated.openapi.apis.MdbQuotasApi;
import com.onyxdb.mdb.generated.openapi.models.ExchangeQuotasBetweenProductsRequest;
import com.onyxdb.mdb.generated.openapi.models.ListMdbResourcesRequest;
import com.onyxdb.mdb.generated.openapi.models.ListQuotasResponse;
import com.onyxdb.mdb.generated.openapi.models.ProductQuotas;
import com.onyxdb.mdb.generated.openapi.models.Quota;
import com.onyxdb.mdb.generated.openapi.models.QuotaToExchange;
import com.onyxdb.mdb.generated.openapi.models.Resource;
import com.onyxdb.mdb.generated.openapi.models.SimulateMongoDBQuotaUsageRequest;
import com.onyxdb.mdb.generated.openapi.models.SimulateMongoDBQuotaUsageResponse;
import com.onyxdb.mdb.generated.openapi.models.SimulateQuotasExchangeBetweenProductsResponse;
import com.onyxdb.mdb.generated.openapi.models.UploadQuotasToProductsRequest;

@RestController
public class MdbQuotaController implements MdbQuotasApi {
    private static final Quota CPU_QUOTA = new Quota(
            UUID.randomUUID(),
            UUID.randomUUID(),
            1L,
            0L,
            1L
    );
    private static final Quota RAM_QUOTA = new Quota(
            UUID.randomUUID(),
            UUID.randomUUID(),
            1L,
            0L,
            1L
    );
    private static final QuotaToExchange CPU_TO_EXCHANGE = new QuotaToExchange(
            UUID.randomUUID(),
            UUID.randomUUID(),
            1L
    );
    private static final QuotaToExchange RAM_TO_EXCHANGE = new QuotaToExchange(
            UUID.randomUUID(),
            UUID.randomUUID(),
            1L
    );

    @Override
    public ResponseEntity<Void> exchangeQuotasBetweenProducts(ExchangeQuotasBetweenProductsRequest rq) {
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ListQuotasResponse> listQuotasByProduct(UUID productId) {
        return ResponseEntity.ok(
                new ListQuotasResponse(
                        List.of(CPU_QUOTA, RAM_QUOTA)
                )
        );
    }

    @Override
    public ResponseEntity<ListMdbResourcesRequest> listResources() {
        return ResponseEntity.ok(
                new ListMdbResourcesRequest(
                        List.of(
                                new Resource(
                                        UUID.randomUUID(),
                                        "vcpu",
                                        "Количество vCPU"
                                ),
                                new Resource(
                                        UUID.randomUUID(),
                                        "ram",
                                        "Объем оперативной памяти"
                                )
                        )
                )
        );
    }

    @Override
    public ResponseEntity<SimulateMongoDBQuotaUsageResponse> simulateMongoDbQuotasUsage(
            SimulateMongoDBQuotaUsageRequest rq
    ) {
        return ResponseEntity.ok(
                new SimulateMongoDBQuotaUsageResponse(
                        List.of(CPU_QUOTA, RAM_QUOTA)
                )
        );
    }

    @Override
    public ResponseEntity<SimulateQuotasExchangeBetweenProductsResponse> simulateQuotasExchangeBetweenProducts(ExchangeQuotasBetweenProductsRequest exchangeQuotasBetweenProductsRequest) {
        return ResponseEntity.ok(
                new SimulateQuotasExchangeBetweenProductsResponse(
                        new ProductQuotas(
                                UUID.randomUUID(),
                                List.of(CPU_TO_EXCHANGE, RAM_TO_EXCHANGE)
                        ),
                        new ProductQuotas(
                                UUID.randomUUID(),
                                List.of(CPU_TO_EXCHANGE, RAM_TO_EXCHANGE)
                        )
                )
        );
    }

    @Override
    public ResponseEntity<Void> uploadQuotasToProducts(UploadQuotasToProductsRequest uploadQuotasToProductsRequest) {
        return ResponseEntity.ok().build();
    }
}
