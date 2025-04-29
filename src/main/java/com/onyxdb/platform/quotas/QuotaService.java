package com.onyxdb.platform.quotas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.core.clusters.models.ClusterConfig;
import com.onyxdb.platform.core.resourcePresets.ResourcePreset;
import com.onyxdb.platform.core.resourcePresets.ResourcePresetService;
import com.onyxdb.platform.exceptions.BadRequestException;
import com.onyxdb.platform.projects.Project;
import com.onyxdb.platform.projects.ProjectService;
import com.onyxdb.platform.resources.Resource;
import com.onyxdb.platform.resources.ResourceFilter;
import com.onyxdb.platform.resources.ResourceService;
import com.onyxdb.platform.resources.ResourceType;

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

    public List<EnrichedProductQuota> simulateMongoDbQuotasUsage(
            UUID projectId,
            ClusterConfig clusterConfig
    ) {
        ResourcePreset preset = resourcePresetService.getOrThrow(clusterConfig.resources().presetId());
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

        Project project = projectService.get(projectId);
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

        return List.of(
                vcpuQuota.addUsage(Double.valueOf(Math.ceil(preset.vcpu())).longValue() * clusterConfig.replicas()),
                ramQuota.addUsage(Double.valueOf(Math.ceil(preset.ram())).longValue()  * clusterConfig.replicas())
        );
    }
}
