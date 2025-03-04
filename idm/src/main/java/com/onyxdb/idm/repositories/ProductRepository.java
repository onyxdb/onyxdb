package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.Product;
import com.onyxdb.idm.models.ProductTree;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author ArtemFed
 */
public interface ProductRepository {
    Optional<Product> findById(UUID id);

    List<Product> findRootProducts();

    List<Product> findChildren(UUID productId);

    List<ProductTree> findChildrenTree(UUID productId, int depth);

    List<Product> findAll();

    void create(Product product);

    void update(Product product);

    void delete(UUID id);
}