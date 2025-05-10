package com.onyxdb.platform.mdb.quotas;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.onyxdb.platform.generated.jooq.tables.records.ProductQuotasRecord;
import com.onyxdb.platform.generated.openapi.models.ProductQuotasDTO;
import com.onyxdb.platform.generated.openapi.models.UploadQuotasToProductsRequestDTO;
import com.onyxdb.platform.mdb.resources.Resource;
import com.onyxdb.platform.mdb.resources.ResourceMapper;

public class QuotaMapper {
    public ProductQuota map(ProductQuotasRecord r) {
        return new ProductQuota(
                r.getProductId(),
                r.getResourceId(),
                r.getLimit(),
                r.getAllocation(),
                r.getFree()
        );
    }

    public List<ProductQuotaToUpload> map(UploadQuotasToProductsRequestDTO rq) {
        return rq.getProducts().stream()
                .flatMap(p -> p.getQuotas()
                        .stream()
                        .map(q -> new ProductQuotaToUpload(
                                p.getProductId(),
                                q.getResourceId(),
                                q.getLimit()
                        ))
                )
                .toList();
    }

    public ProductQuotasRecord map(ProductQuotaToUpload q) {
        return new ProductQuotasRecord(
                q.productId(),
                q.resourceId(),
                q.limit(),
                0L,
                q.limit()
        );
    }

    public QuotaToTransfer mapToQuotaExchange(com.onyxdb.platform.generated.openapi.models.QuotaToTransferDTO q) {
        return new QuotaToTransfer(
                q.getResourceId(),
                q.getLimit()
        );
    }

    public List<com.onyxdb.platform.generated.openapi.models.QuotaDTO> mapToQuotaList(
            List<ProductQuota> quotas,
            List<Resource> resources,
            ResourceMapper resourceMapper
    ) {
        Map<UUID, Resource> resourceIdToResource = resources.stream()
                .collect(Collectors.toMap(Resource::id, r -> r));

        return quotas.stream().map(q -> {
                    if (!resourceIdToResource.containsKey(q.resourceId())) {
                        throw new RuntimeException("Can't find resource with id " + q.resourceId());
                    }

                    return new com.onyxdb.platform.generated.openapi.models.QuotaDTO(
                            resourceMapper.resourceToResourceDTO(resourceIdToResource.get(q.resourceId())),
                            q.limit(),
                            q.usage(),
                            q.free()
                    );
                })
                .toList();
    }

    public com.onyxdb.platform.generated.openapi.models.QuotaDTO mapToQuotaResponse(
            EnrichedProductQuota q,
            ResourceMapper resourceMapper
    ) {
        return new com.onyxdb.platform.generated.openapi.models.QuotaDTO(
                resourceMapper.resourceToResourceDTO(q.resource()),
                q.limit(),
                q.usage(),
                q.free()
        );
    }

    public List<ProductQuotasDTO> mapToProductQuotasListResponse(
            List<EnrichedProductQuota> qs,
            ResourceMapper resourceMapper
    ) {
        Map<UUID, List<EnrichedProductQuota>> productIdToQuota = qs.stream()
                .collect(Collectors.groupingBy(EnrichedProductQuota::productId));

        return productIdToQuota.entrySet()
                .stream()
                .map(e -> new ProductQuotasDTO(
                        e.getKey(),
                        e.getValue().stream().map(q -> mapToQuotaResponse(q, resourceMapper)).toList()
                ))
                .toList();
    }

    public ProductQuotasDTO mapToProductQuotasListResponse(
            UUID productId,
            List<EnrichedProductQuota> qs,
            ResourceMapper resourceMapper
    ) {
        return new ProductQuotasDTO(
                productId,
                qs.stream().map(q -> mapToQuotaResponse(q, resourceMapper)).toList()
        );
    }

    public List<ProductQuotasDTO> mapToProductQuotasList(
            List<ProductQuota> productQuotas,
            List<Resource> resources,
            ResourceMapper resourceMapper
    ) {
        Map<UUID, Resource> resourceIdToResource = resources.stream()
                .collect(Collectors.toMap(Resource::id, r -> r));

        Map<UUID, List<ProductQuota>> productIdToProductQuotas = productQuotas.stream()
                .collect(Collectors.groupingBy(ProductQuota::productId));

        return productIdToProductQuotas.entrySet()
                .stream()
                .map(e -> {
                    UUID productId = e.getKey();
                    List<com.onyxdb.platform.generated.openapi.models.QuotaDTO> quotas = e.getValue().stream().map(q -> {
                                if (!resourceIdToResource.containsKey(q.resourceId())) {
                                    throw new RuntimeException("Can't find resource with id " + q.resourceId());
                                }

                                return new com.onyxdb.platform.generated.openapi.models.QuotaDTO(
                                        resourceMapper.resourceToResourceDTO(resourceIdToResource.get(q.resourceId())),
                                        q.limit(),
                                        q.usage(),
                                        q.free()
                                );
                            })
                            .toList();

                    return new ProductQuotasDTO(
                            productId,
                            quotas
                    );
                })
                .toList();
    }

//    public ProductQuotas mapToProductQuotas(
//            UUID productId,
//            List<Quota> quotas,
//            Resource resource,
//            ResourceMapper resourceMapper
//    ) {
//        return new ProductQuotas(
//                productId,
//                mapToQuotaList(quotas)
//        );
//    }

    public ProductQuotaToUpload mapToProductQuotaToUpload(UUID productId, QuotaToTransfer q) {
        return new ProductQuotaToUpload(
                productId,
                q.resourceId(),
                q.limit()
        );
    }

//    public com.onyxdb.mdb.generated.openapi.models.Quota toQuota(
//            Quota q,
//            Resource resource,
//            ResourceMapper resourceMapper
//    ) {
//        return new com.onyxdb.mdb.generated.openapi.models.Quota(
//                resourceMapper.map(resource),
//                q.limit(),
//                q.usage(),
//                q.free()
//        );
//    }
}
