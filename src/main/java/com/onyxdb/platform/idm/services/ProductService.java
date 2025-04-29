package com.onyxdb.platform.idm.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import com.onyxdb.platform.idm.v1.ResourceNotFoundException;
import com.onyxdb.platform.idm.models.PaginatedResult;
import com.onyxdb.platform.idm.models.Product;
import com.onyxdb.platform.idm.models.ProductTree;
import com.onyxdb.platform.idm.repositories.ProductRepository;

/**
 * @author ArtemFed
 */
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product findById(UUID id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    public PaginatedResult<Product> findAll(String search, Integer limit, Integer offset) {
        return productRepository.findAll(search, limit, offset);
    }

    public List<Product> findRootProducts() {
        return productRepository.findRootProducts();
    }

    public List<Product> findChildren(UUID productId) {
        return productRepository.findChildren(productId);
    }

    public List<ProductTree> findAllTrees() {
        List<Product> roots = findRootProducts();
        List<ProductTree> trees = new ArrayList<>();
        for (Product root : roots) {
            Product product = productRepository.findById(root.id()).orElseThrow();
            List<ProductTree> children = productRepository.findChildrenTree(product.id(), 99);
            trees.add(new ProductTree(product, children));
        }
        return trees;
    }

    public ProductTree findChildrenTree(UUID productId, Integer depth) {
        var product = productRepository.findById(productId).orElseThrow();
        var children = productRepository.findChildrenTree(productId, depth);
        return new ProductTree(product, children);
    }

    public List<Product> findAllParentProducts(UUID productId) {
        return productRepository.findAllParentProducts(productId);
    }

    public Product create(Product product) {
        return productRepository.create(product);
    }

    public Product update(Product product) {
        return productRepository.update(product);
    }

    public void delete(UUID id) {
        productRepository.delete(id);
    }
}