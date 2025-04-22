package com.onyxdb.mdb.controllers;

import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.mdb.core.clusters.ClusterMapper;
import com.onyxdb.mdb.core.clusters.models.ClusterConfig;
import com.onyxdb.mdb.generated.openapi.apis.MdbQuotasApi;
import com.onyxdb.mdb.generated.openapi.models.ListQuotasByProductsResponse;
import com.onyxdb.mdb.generated.openapi.models.ListResourcesResponse;
import com.onyxdb.mdb.generated.openapi.models.SimulateMongoDBQuotasUsageRequest;
import com.onyxdb.mdb.generated.openapi.models.SimulateMongoDBQuotasUsageResponse;
import com.onyxdb.mdb.generated.openapi.models.SimulateTransferQuotasBetweenProductsResponse;
import com.onyxdb.mdb.generated.openapi.models.TransferQuotasBetweenProductsRequest;
import com.onyxdb.mdb.generated.openapi.models.UploadQuotasToProductsRequest;
import com.onyxdb.mdb.quotas.EnrichedProductQuota;
import com.onyxdb.mdb.quotas.ProductQuotaToUpload;
import com.onyxdb.mdb.quotas.QuotaFilter;
import com.onyxdb.mdb.quotas.QuotaMapper;
import com.onyxdb.mdb.quotas.QuotaService;
import com.onyxdb.mdb.quotas.QuotaToTransfer;
import com.onyxdb.mdb.quotas.SimulateTransferQuotasBetweenProductsResult;
import com.onyxdb.mdb.resources.Resource;
import com.onyxdb.mdb.resources.ResourceFilter;
import com.onyxdb.mdb.resources.ResourceMapper;
import com.onyxdb.mdb.resources.ResourceService;

@RestController
public class MdbQuotaController implements MdbQuotasApi {
    private final ResourceService resourceService;
    private final ResourceMapper resourceMapper;
    private final QuotaService quotaService;
    private final QuotaMapper quotaMapper;
    private final ClusterMapper clusterMapper;

    public MdbQuotaController(
            ResourceService resourceService,
            ResourceMapper resourceMapper,
            QuotaService quotaService,
            QuotaMapper quotaMapper,
            ClusterMapper clusterMapper
    ) {
        this.resourceService = resourceService;
        this.resourceMapper = resourceMapper;
        this.quotaService = quotaService;
        this.quotaMapper = quotaMapper;
        this.clusterMapper = clusterMapper;
    }

    @Override
    public ResponseEntity<ListQuotasByProductsResponse> listQuotasByProducts(
            @Nullable
            List<UUID> productIds
    ) {
        QuotaFilter filter = QuotaFilter.builder()
                .withProductIds(productIds)
                .build();
        List<EnrichedProductQuota> quotas = quotaService.listProductQuotas(filter);

        var response = new ListQuotasByProductsResponse(
                quotaMapper.mapToProductQuotasListResponse(quotas, resourceMapper)
        );
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> transferQuotasBetweenProducts(TransferQuotasBetweenProductsRequest rq) {
        List<QuotaToTransfer> quotas = rq.getQuotas().stream().map(quotaMapper::mapToQuotaExchange).toList();
        quotaService.transferQuotasBetweenProducts(
                rq.getSrcProductId(),
                rq.getDstProductId(),
                quotas
        );

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<SimulateTransferQuotasBetweenProductsResponse> simulateTransferQuotasBetweenProducts(
            TransferQuotasBetweenProductsRequest rq
    ) {
        SimulateTransferQuotasBetweenProductsResult result = quotaService.simulateTransferQuotasBetweenProducts(
                rq.getSrcProductId(),
                rq.getDstProductId(),
                rq.getQuotas().stream().map(quotaMapper::mapToQuotaExchange).toList()
        );

        return ResponseEntity.ok(
                new SimulateTransferQuotasBetweenProductsResponse(
                        quotaMapper.mapToProductQuotasListResponse(rq.getSrcProductId(), result.srcQuotas(), resourceMapper),
                        quotaMapper.mapToProductQuotasListResponse(rq.getDstProductId(), result.dstQuotas(), resourceMapper)
                )
        );
    }

    @Override
    public ResponseEntity<ListResourcesResponse> listResources() {
        List<Resource> resources = resourceService.listResources(ResourceFilter.empty());

        var response = new ListResourcesResponse(
                resources.stream().map(resourceMapper::map).toList()
        );
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<SimulateMongoDBQuotasUsageResponse> simulateMongoDbQuotasUsage(
            SimulateMongoDBQuotasUsageRequest rq
    ) {
        ClusterConfig clusterConfig = clusterMapper.mapToClusterConfig(rq.getConfig());
        List<EnrichedProductQuota> simulatedQuotas = quotaService.simulateMongoDbQuotasUsage(
                rq.getProjectId(),
                clusterConfig
        );

        var response = new SimulateMongoDBQuotasUsageResponse(
                simulatedQuotas.stream().map(q -> quotaMapper.mapToQuotaResponse(q, resourceMapper)).toList()
        );
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> uploadQuotasToProducts(UploadQuotasToProductsRequest rq) {
        List<ProductQuotaToUpload> quotas = quotaMapper.map(rq);
        quotaService.uploadProductQuotas(quotas);

        return ResponseEntity.ok().build();
    }
}
