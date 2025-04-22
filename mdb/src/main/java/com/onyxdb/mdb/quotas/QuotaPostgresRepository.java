package com.onyxdb.mdb.quotas;

import java.util.List;
import java.util.UUID;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import com.onyxdb.mdb.generated.jooq.tables.records.ProductQuotasRecord;
import com.onyxdb.mdb.resources.ResourceMapper;

import static com.onyxdb.mdb.generated.jooq.Tables.PRODUCTS;
import static com.onyxdb.mdb.generated.jooq.Tables.PRODUCT_QUOTAS;
import static com.onyxdb.mdb.generated.jooq.Tables.RESOURCES;

public class QuotaPostgresRepository implements QuotaRepository {
    private final DSLContext dslContext;
    private final QuotaMapper quotaMapper;
    private final ResourceMapper resourceMapper;

    public QuotaPostgresRepository(
            DSLContext dslContext,
            QuotaMapper quotaMapper,
            ResourceMapper resourceMapper
    ) {
        this.dslContext = dslContext;
        this.quotaMapper = quotaMapper;
        this.resourceMapper = resourceMapper;
    }

    @Override
    public List<EnrichedProductQuota> listProductQuotas(QuotaFilter filter) {
        return dslContext.select(
                        PRODUCTS.ID,
                        PRODUCT_QUOTAS.PRODUCT_ID,
                        RESOURCES.ID,
                        RESOURCES.NAME,
                        RESOURCES.DESCRIPTION,
                        RESOURCES.TYPE,
                        RESOURCES.PROVIDER,
                        DSL.coalesce(PRODUCT_QUOTAS.LIMIT, 0).as("limit"),
                        DSL.coalesce(PRODUCT_QUOTAS.ALLOCATION, 0).as("usage"),
                        DSL.coalesce(PRODUCT_QUOTAS.FREE, 0).as("free")
                )
                .from(PRODUCTS)
                .crossJoin(RESOURCES)
                .leftJoin(PRODUCT_QUOTAS)
                .on(RESOURCES.ID.eq(PRODUCT_QUOTAS.RESOURCE_ID)
                        .and(PRODUCTS.ID.eq(PRODUCT_QUOTAS.PRODUCT_ID))
                )
                .where(filter.buildCondition())
                .fetch(r -> new EnrichedProductQuota(
                        r.get(PRODUCTS.ID),
                        resourceMapper.fromRecord(r),
                        r.get("limit", Long.class),
                        r.get("usage", Long.class),
                        r.get("free", Long.class)
                ));
    }

    @Override
    public void addProductQuotas(List<ProductQuotaToUpload> quotas) {
        List<ProductQuotasRecord> records = quotas.stream().map(quotaMapper::map).toList();

        dslContext.insertInto(PRODUCT_QUOTAS)
                .set(records)
                .onConflict(PRODUCT_QUOTAS.PRODUCT_ID, PRODUCT_QUOTAS.RESOURCE_ID)
                .doUpdate()
                .set(PRODUCT_QUOTAS.LIMIT, PRODUCT_QUOTAS.LIMIT.add(DSL.excluded(PRODUCT_QUOTAS.LIMIT)))
                .set(PRODUCT_QUOTAS.FREE, PRODUCT_QUOTAS.FREE.add(DSL.excluded(PRODUCT_QUOTAS.FREE)))
                .execute();
    }

    @Override
    public void subtractProductQuotas(List<ProductQuotaToUpload> quotas) {
        List<ProductQuotasRecord> records = quotas.stream().map(quotaMapper::map).toList();

        dslContext.insertInto(PRODUCT_QUOTAS)
                .set(records)
                .onConflict(PRODUCT_QUOTAS.PRODUCT_ID, PRODUCT_QUOTAS.RESOURCE_ID)
                .doUpdate()
                .set(PRODUCT_QUOTAS.LIMIT, PRODUCT_QUOTAS.LIMIT.subtract(DSL.excluded(PRODUCT_QUOTAS.LIMIT)))
                .set(PRODUCT_QUOTAS.FREE, PRODUCT_QUOTAS.FREE.subtract(DSL.excluded(PRODUCT_QUOTAS.FREE)))
                .execute();
    }

    @Override
    public void addProductQuotas(UUID toProductId, List<QuotaToTransfer> quotas) {
        List<ProductQuotaToUpload> quotaToUpload = quotas.stream()
                .map(q -> quotaMapper.mapToProductQuotaToUpload(toProductId, q))
                .toList();

        addProductQuotas(quotaToUpload);
    }

    @Override
    public void subtractProductQuotas(UUID fromProductId, List<QuotaToTransfer> quotas) {
        List<ProductQuotaToUpload> quotaToUpload = quotas.stream()
                .map(q -> quotaMapper.mapToProductQuotaToUpload(fromProductId, q))
                .toList();

        subtractProductQuotas(quotaToUpload);
    }
}
