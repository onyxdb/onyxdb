package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.ProductTable;
import com.onyxdb.idm.models.Product;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author ArtemFed
 */
@Repository
@RequiredArgsConstructor
public class ProductPostgresRepository implements ProductRepository {
    private final DSLContext dslContext;
    private final static ProductTable productTable = Tables.PRODUCT_TABLE;

    @Override
    public Optional<Product> findById(UUID id) {
        return dslContext.selectFrom(productTable)
                .where(productTable.ID.eq(id))
                .fetchOptional(Product::fromDAO);
    }

    @Override
    public List<Product> findAll() {
        return dslContext.selectFrom(productTable)
                .fetch(Product::fromDAO);
    }

    @Override
    public void create(Product product) {
        dslContext.executeInsert(product.toDAO());
    }

    @Override
    public void update(Product product) {
        dslContext.update(productTable)
                .set(productTable.NAME, product.name())
                .set(productTable.DESCRIPTION, product.description())
                .set(productTable.UPDATED_AT, product.updatedAt())
                .set(productTable.PARENT_ID, product.parent_id())
                .set(productTable.DATA, product.getDataAsJsonb())
                .set(productTable.OWNER_ID, product.ownerId())
                .where(productTable.ID.eq(product.id()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(productTable)
                .where(productTable.ID.eq(id))
                .execute();
    }
}