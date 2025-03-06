package com.onyxdb.idm.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.ProductTable;
import com.onyxdb.idm.models.Product;
import com.onyxdb.idm.models.ProductTree;

/**
 * @author ArtemFed
 */
@Repository
@RequiredArgsConstructor
public class ProductPostgresRepository implements ProductRepository {
    private final static ProductTable productTable = Tables.PRODUCT_TABLE;
    private final DSLContext dslContext;

    @Override
    public List<Product> findChildren(UUID productId) {
        return fetchChildrenFromDb(productId);
    }

    @Override
    public List<Product> findRootProducts() {
        return dslContext.selectFrom(productTable)
                .where(productTable.PARENT_ID.isNull()
                        .or(productTable.PARENT_ID.eq(productTable.ID)))
                .fetch(Product::fromDAO);
    }

    @Override
    public List<ProductTree> findChildrenTree(UUID productId, Integer depth) {
        if (depth == null) depth = 100;
        if (depth <= 0) return List.of();
        List<Product> children = findChildren(productId);
        Integer finalDepth = depth;
        return children.stream()
                .map(child -> new ProductTree(child, findChildrenTree(child.id(), finalDepth - 1)))
                .collect(Collectors.toList());
    }

    private List<Product> fetchChildrenFromDb(UUID parentId) {
        return dslContext.selectFrom(productTable)
                .where(productTable.PARENT_ID.eq(parentId))
                .fetchInto(Product.class);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return dslContext.selectFrom(productTable)
                .where(productTable.ID.eq(id))
                .fetchOptional(Product::fromDAO);
    }

    private List<Product> fetchProductTree(UUID productId, Integer depth) {
        return dslContext.fetch("""
                    WITH RECURSIVE product_hierarchy AS (
                        SELECT *, 1 AS level FROM product_table WHERE id = ?
                        UNION ALL
                        SELECT p.*, ph.level + 1
                        FROM product_table p
                        INNER JOIN product_hierarchy ph ON p.parent_id = ph.id
                        WHERE ph.level < ?
                    )
                    SELECT * FROM product_hierarchy;
                """, productId, depth).into(Product.class);
    }

    @Override
    public List<Product> findAll() {
        return dslContext.selectFrom(productTable)
                .fetch(Product::fromDAO);
    }

    @Override
    public Product create(Product product) {
        var record = dslContext.insertInto(productTable)
                .set(productTable.ID, UUID.randomUUID())
                .set(productTable.NAME, product.name())
                .set(productTable.DESCRIPTION, product.description())
                .set(productTable.PARENT_ID, product.parent_id())
                .set(productTable.OWNER_ID, product.ownerId())
                .set(productTable.DATA, product.getDataAsJsonb())
                .set(productTable.CREATED_AT, LocalDateTime.now())
                .set(productTable.UPDATED_AT, LocalDateTime.now())
                .returning()
                .fetchOne();

        assert record != null;
        return Product.fromDAO(record);
    }

    @Override
    public Product update(Product product) {
        var record = dslContext.update(productTable)
                .set(productTable.NAME, product.name())
                .set(productTable.DESCRIPTION, product.description())
                .set(productTable.PARENT_ID, product.parent_id())
                .set(productTable.OWNER_ID, product.ownerId())
                .set(productTable.DATA, product.getDataAsJsonb())
                .set(productTable.UPDATED_AT, LocalDateTime.now())
                .where(productTable.ID.eq(product.id()))
                .returning()
                .fetchOne();

        assert record != null;
        return Product.fromDAO(record);
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(productTable)
                .where(productTable.ID.eq(id))
                .execute();
    }
}