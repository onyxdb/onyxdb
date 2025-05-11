package com.onyxdb.platform.idm.controllers;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.ProductsApi;
import com.onyxdb.platform.generated.openapi.models.PaginatedProductResponse;
import com.onyxdb.platform.generated.openapi.models.ProductDTO;
import com.onyxdb.platform.generated.openapi.models.ProductPostDTO;
import com.onyxdb.platform.generated.openapi.models.ProductTreeDTO;
import com.onyxdb.platform.idm.common.PermissionCheck;
import com.onyxdb.platform.idm.models.PaginatedResult;
import com.onyxdb.platform.idm.models.Product;
import com.onyxdb.platform.idm.models.ProductTree;
import com.onyxdb.platform.idm.services.ProductService;

/**
 * @author ArtemFed
 */
@RestController
@RequiredArgsConstructor
public class ProductsController implements ProductsApi {
    private final ProductService productService;

    @Override
    @PermissionCheck(entity = "product", action = "create")
    public ResponseEntity<ProductDTO> createProduct(@Valid ProductPostDTO productDTO) {
        Product product = Product.fromPostDTO(productDTO);
        Product createdProduct = productService.create(product);
        return new ResponseEntity<>(createdProduct.toDTO(), HttpStatus.CREATED);
    }

    @Override
    @PermissionCheck(entity = "product", action = "delete", resourceId = "#productId")
    public ResponseEntity<Void> deleteProduct(UUID productId) {
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PermissionCheck(entity = "product", action = "get")
    public ResponseEntity<List<ProductTreeDTO>> getAllProductTree() {
        List<ProductTree> productTrees = productService.findAllTrees();
        List<ProductTreeDTO> productTreesDTOs = productTrees.stream().map(ProductTree::toDTO).toList();
        return ResponseEntity.ok(productTreesDTOs);
    }

    @Override
    @PermissionCheck(entity = "product", action = "get")
    public ResponseEntity<PaginatedProductResponse> getAllProducts(String search, Integer limit, Integer offset) {
        PaginatedResult<Product> products = productService.findAll(search, limit, offset);
        List<ProductDTO> productDTOs = products.data().stream().map(Product::toDTO).toList();
        return ResponseEntity.ok(new PaginatedProductResponse()
                .data(productDTOs)
                .totalCount(products.totalCount())
                .startPosition(products.startPosition())
                .endPosition(products.endPosition())
        );
    }

    @Override
    @PermissionCheck(entity = "product", action = "get", resourceId = "#productId")
    public ResponseEntity<ProductDTO> getProductById(UUID productId) {
        Product product = productService.findById(productId);
        return ResponseEntity.ok(product.toDTO());
    }

    @Override
    @PermissionCheck(entity = "product", action = "get", resourceId = "#productId")
    public ResponseEntity<List<ProductDTO>> getProductChildren(UUID productId) {
        List<Product> products = productService.findChildren(productId);
        List<ProductDTO> productDTOs = products.stream().map(Product::toDTO).toList();
        return ResponseEntity.ok(productDTOs);
    }

    @Override
    @PermissionCheck(entity = "product", action = "get", resourceId = "#productId")
    public ResponseEntity<List<ProductDTO>> getProductParents(UUID productId) {
        List<Product> products = productService.findAllParentProducts(productId);
        List<ProductDTO> productDTOs = products.stream().map(Product::toDTO).toList();
        return ResponseEntity.ok(productDTOs);
    }

    @Override
    @PermissionCheck(entity = "product", action = "get", resourceId = "#productId")
    public ResponseEntity<ProductTreeDTO> getProductTree(UUID productId, Integer depth) {
        ProductTree productTree = productService.findChildrenTree(productId, depth);
        return ResponseEntity.ok(productTree.toDTO());
    }

    @Override
    @PermissionCheck(entity = "product", action = "get")
    public ResponseEntity<List<ProductDTO>> getProductsRoots() {
        List<Product> products = productService.findRootProducts();
        List<ProductDTO> productDTOs = products.stream().map(Product::toDTO).toList();
        return ResponseEntity.ok(productDTOs);
    }

    @Override
    @PermissionCheck(entity = "product", action = "update", resourceId = "#productId")
    public ResponseEntity<ProductDTO> updateProduct(UUID productId, @Valid ProductPostDTO productDTO) {
        productDTO.setId(productId);
        Product product = Product.fromPostDTO(productDTO);
        Product updatedProduct = productService.update(product);
        return ResponseEntity.ok(updatedProduct.toDTO());
    }
}
