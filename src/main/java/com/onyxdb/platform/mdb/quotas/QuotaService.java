package com.onyxdb.platform.mdb.quotas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.mdb.clusters.models.ClusterConfig;
import com.onyxdb.platform.mdb.exceptions.BadRequestException;
import com.onyxdb.platform.mdb.projects.Project;
import com.onyxdb.platform.mdb.projects.ProjectService;
import com.onyxdb.platform.mdb.resourcePresets.ResourcePreset;
import com.onyxdb.platform.mdb.resourcePresets.ResourcePresetService;
import com.onyxdb.platform.mdb.resources.Resource;
import com.onyxdb.platform.mdb.resources.ResourceFilter;
import com.onyxdb.platform.mdb.resources.ResourceService;
import com.onyxdb.platform.mdb.resources.ResourceType;

public class QuotaService {
    private final QuotaRepository quotaRepository;
    private final TransactionTemplate transactionTemplate;
    private final QuotaMapper quotaMapper;
    private final ResourcePresetService resourcePresetService;
    private final ResourceService resourceService;
    private final ProjectService projectService;

    public QuotaService(
            QuotaRepository quotaRepository,
            TransactionTemplate transactionTemplate,
            QuotaMapper quotaMapper,
            ResourcePresetService resourcePresetService,
            ResourceService resourceService,
            ProjectService projectService
    ) {
        this.quotaRepository = quotaRepository;
        this.transactionTemplate = transactionTemplate;
        this.quotaMapper = quotaMapper;
        this.resourcePresetService = resourcePresetService;
        this.resourceService = resourceService;
        this.projectService = projectService;
    }

    public List<EnrichedProductQuota> listProductQuotas(QuotaFilter filter) {
        return quotaRepository.listProductQuotas(filter);
    }

    public void uploadProductQuotas(List<ProductQuotaToUpload> quotas) {
        quotaRepository.addProductQuotas(quotas);
    }

    public void transferQuotasBetweenProducts(
            UUID srcProductId,
            UUID dstProductId,
            List<QuotaToTransfer> quotas
    ) {
        // TODO use select for update to get lock updating quotas
        validateTransferQuotasBetweenProducts(srcProductId, dstProductId, quotas);

        transactionTemplate.executeWithoutResult(status -> {
            quotaRepository.subtractProductQuotas(srcProductId, quotas);
            quotaRepository.addProductQuotas(dstProductId, quotas);
        });
    }

    public void validateTransferQuotasBetweenProducts(
            UUID srcProductId,
            UUID dstProductId,
            List<QuotaToTransfer> quotasToTransfer
    ) {
        QuotaFilter filter = QuotaFilter.builder().withProductId(srcProductId).build();
        List<EnrichedProductQuota> srcQuotas = listProductQuotas(filter);

        Map<ProductQuotaId, EnrichedProductQuota> quotaIdToSrcQuota = srcQuotas.stream()
                .collect(Collectors.toMap(
                        q -> new ProductQuotaId(q.productId(), q.resource().id()),
                        q -> q
                ));

        quotasToTransfer.forEach(quotaToTransfer -> {
            var srcQuotaId = new ProductQuotaId(srcProductId, quotaToTransfer.resourceId());
            if (!quotaIdToSrcQuota.containsKey(srcQuotaId)) {
                throw new BadRequestException(String.format(
                        "Can't find source quota id '%s' ", srcQuotaId
                ));
            }

            EnrichedProductQuota srcQuota = quotaIdToSrcQuota.get(srcQuotaId);

            if (quotaToTransfer.limit() > srcQuota.free()) {
                throw new BadRequestException(String.format(
                        "Can't transfer quota from product id '%s' to product id '%s' because source product " +
                                "has %d free quota for resource id '%s', but was request %d",
                        srcProductId,
                        dstProductId,
                        srcQuota.free(),
                        quotaToTransfer.resourceId(),
                        quotaToTransfer.limit()
                ));
            }
        });
    }

    public SimulateTransferQuotasBetweenProductsResult simulateTransferQuotasBetweenProducts(
            UUID srcProductId,
            UUID dstProductId,
            List<QuotaToTransfer> quotasToTransfer
    ) {
        QuotaFilter srcFilter = QuotaFilter.builder().withProductId(srcProductId).build();
        QuotaFilter dstFilter = QuotaFilter.builder().withProductId(dstProductId).build();
        List<EnrichedProductQuota> srcQuotas = listProductQuotas(srcFilter);
        List<EnrichedProductQuota> dstQuotas = listProductQuotas(dstFilter);
        List<EnrichedProductQuota> simulatedSrcQuotas = new ArrayList<>(srcQuotas.size());
        List<EnrichedProductQuota> simulatedDstQuotas = new ArrayList<>(srcQuotas.size());

        Map<ProductQuotaId, EnrichedProductQuota> quotaIdToSrcQuota = srcQuotas.stream()
                .collect(Collectors.toMap(
                        q -> new ProductQuotaId(q.productId(), q.resource().id()),
                        q -> q
                ));
        Map<ProductQuotaId, EnrichedProductQuota> quotaIdToDstQuota = dstQuotas.stream()
                .collect(Collectors.toMap(
                        q -> new ProductQuotaId(q.productId(), q.resource().id()),
                        q -> q
                ));

        quotasToTransfer.forEach(quotaToTransfer -> {
            var srcQuotaId = new ProductQuotaId(srcProductId, quotaToTransfer.resourceId());
            var dstQuotaId = new ProductQuotaId(dstProductId, quotaToTransfer.resourceId());
            if (!quotaIdToSrcQuota.containsKey(srcQuotaId)) {
                throw new BadRequestException(String.format(
                        "Can't find source quota id '%s' ", srcQuotaId
                ));
            }

            EnrichedProductQuota srcQuota = quotaIdToSrcQuota.get(srcQuotaId);
            EnrichedProductQuota dstQuota = quotaIdToDstQuota.getOrDefault(
                    dstQuotaId,
                    EnrichedProductQuota.empty(dstQuotaId.productId(), srcQuota.resource())
            );

            simulatedSrcQuotas.add(srcQuota.subtract(quotaToTransfer));
            simulatedDstQuotas.add(dstQuota.add(quotaToTransfer));
        });

        return new SimulateTransferQuotasBetweenProductsResult(
                simulatedSrcQuotas,
                simulatedDstQuotas
        );
    }

    public void applyQuotaByClusterConfig(
            UUID projectId,
            @Nullable
            ClusterConfig currentClusterConfig,
            @Nullable
            ClusterConfig previousClusterConfig
    ) {
        List<EnrichedProductQuota> quotas = simulateMongoDbQuotasUsage(
                projectId,
                currentClusterConfig,
                previousClusterConfig
        );

        // if quota was super negative, then we can't update quotas resizing to smaller or deleting cluster
        quotas.forEach(q -> {
            if (q.free() < 0) {
                throw new BadRequestException(String.format(
                        "Quota is exhausted. Requires extra %d %s of resource '%s' (id='%s')",
                        Math.abs(q.free()),
                        q.resource().type().getUnit().value(),
                        q.resource().name(),
                        q.resource().id()
                ));
            }
        });

        List<ProductQuota> productQuotas = quotas.stream().map(quotaMapper::enrichedProductQuotaToProductQuota).toList();
        quotaRepository.updateProductQuotas(productQuotas);
    }

    public List<EnrichedProductQuota> simulateMongoDbQuotasUsage(
            UUID projectId,
            @Nullable
            ClusterConfig currentClusterConfig,
            @Nullable
            ClusterConfig previousClusterConfig
    ) {
        Resource cpuResource = resourceService.getOrThrow(
                ResourceFilter.builder()
                        .withType(ResourceType.VCPU)
                        .withProvider(QuotaProvider.MDB)
                        .build()
        );
        Resource ramResource = resourceService.getOrThrow(
                ResourceFilter.builder()
                        .withType(ResourceType.RAM)
                        .withProvider(QuotaProvider.MDB)
                        .build()
        );

        Project project = projectService.getProjectOrThrow(projectId, false);
        UUID productId = project.productId();
        List<EnrichedProductQuota> productQuotas = listProductQuotas(QuotaFilter.builder()
                .withProductId(productId)
                .build());

        Map<ProductQuotaId, EnrichedProductQuota> quotaIdToQuota = productQuotas.stream()
                .collect(Collectors.toMap(
                        q -> new ProductQuotaId(q.productId(), q.resource().id()),
                        q -> q
                ));

        EnrichedProductQuota vcpuQuota = quotaIdToQuota.getOrDefault(
                new ProductQuotaId(productId, cpuResource.id()),
                EnrichedProductQuota.empty(productId, cpuResource)
        );
        EnrichedProductQuota ramQuota = quotaIdToQuota.getOrDefault(
                new ProductQuotaId(productId, ramResource.id()),
                EnrichedProductQuota.empty(productId, ramResource)
        );

        if (currentClusterConfig != null) {
            ResourcePreset currentPreset = resourcePresetService.getOrThrow(currentClusterConfig.resources().presetId());
            vcpuQuota = vcpuQuota.addUsage(Double.valueOf(Math.ceil(currentPreset.vcpu())).longValue() * currentClusterConfig.replicas());
            ramQuota = ramQuota.addUsage(Double.valueOf(Math.ceil(currentPreset.ram())).longValue() * currentClusterConfig.replicas());
        }

        if (previousClusterConfig != null) {
            ResourcePreset previousPreset = resourcePresetService.getOrThrow(previousClusterConfig.resources().presetId());
            vcpuQuota = vcpuQuota.subtractUsage(
                    Double.valueOf(Math.ceil(previousPreset.vcpu())).longValue() * previousClusterConfig.replicas()
            );
            ramQuota = ramQuota.subtractUsage(
                    Double.valueOf(Math.ceil(previousPreset.ram())).longValue() * previousClusterConfig.replicas()
            );
        }

        return List.of(
                vcpuQuota,
                ramQuota
        );
    }
}
