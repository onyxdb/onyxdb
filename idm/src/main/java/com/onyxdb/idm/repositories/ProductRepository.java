package com.onyxdb.idm.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.idm.models.Product;
import com.onyxdb.idm.models.ProductTree;

/**
 * @author ArtemFed
 */
public interface ProductRepository {
    Optional<Product> findById(UUID id);

    List<Product> findAll();

    List<Product> findRootProducts();

    List<Product> findChildren(UUID productId);

    List<ProductTree> findChildrenTree(UUID productId, Integer depth);

    List<Product> findAllParentProducts(UUID productId);

    Product create(Product product);

    Product update(Product product);

    void delete(UUID id);
}