package com.onyxdb.idm.services;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.Product;
import com.onyxdb.idm.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    public Product create(Product product) {
        Product forCreate = new Product(
                UUID.randomUUID(),
                product.name(),
                product.description(),
                product.parent_id(),
                product.ownerId(),
                product.data(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        productRepository.create(forCreate);
        return forCreate;
    }

    public Product update(Product product) {
        Product forUpdate = new Product(
                product.id(),
                product.name(),
                product.description(),
                product.parent_id(),
                product.ownerId(),
                product.data(),
                product.createdAt(),
                LocalDateTime.now()
        );
        productRepository.update(forUpdate);
        return forUpdate;
    }

    public void delete(UUID id) {
        productRepository.delete(id);
    }
}