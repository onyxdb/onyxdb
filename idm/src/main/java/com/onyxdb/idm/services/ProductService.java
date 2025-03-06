package com.onyxdb.idm.services;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.Product;
import com.onyxdb.idm.models.ProductTree;
import com.onyxdb.idm.repositories.ProductRepository;

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

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findRootProducts() {
        return productRepository.findRootProducts();
    }

    public List<Product> findChildren(UUID productId) {
        return productRepository.findChildren(productId);
    }

    public ProductTree findChildrenTree(UUID productId, Integer depth) {
        var product = productRepository.findById(productId).orElseThrow();
        var children = productRepository.findChildrenTree(productId, depth);
        return new ProductTree(product, children);
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