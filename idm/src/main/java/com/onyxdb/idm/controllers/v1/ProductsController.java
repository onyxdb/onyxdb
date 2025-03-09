package com.onyxdb.idm.controllers.v1;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.idm.generated.openapi.apis.ProductsApi;
import com.onyxdb.idm.generated.openapi.models.PaginatedProductResponse;
import com.onyxdb.idm.generated.openapi.models.ProductDTO;
import com.onyxdb.idm.generated.openapi.models.ProductTreeDTO;
import com.onyxdb.idm.models.PaginatedResult;
import com.onyxdb.idm.models.Product;
import com.onyxdb.idm.models.ProductTree;
import com.onyxdb.idm.services.ProductService;

/**
 * @author ArtemFed
 */
@RestController
@RequiredArgsConstructor
public class ProductsController implements ProductsApi {
    private final ProductService productService;

    @Override
    public ResponseEntity<ProductDTO> createProduct(@Valid ProductDTO productDTO) {
        Product product = Product.fromDTO(productDTO);
        Product createdProduct = productService.create(product);
        return new ResponseEntity<>(createdProduct.toDTO(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteProduct(UUID productId) {
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<ProductTreeDTO>> getAllProductTree() {
        List<ProductTree> productTrees = productService.findAllTrees();
        List<ProductTreeDTO> productTreesDTOs = productTrees.stream().map(ProductTree::toDTO).toList();
        return ResponseEntity.ok(productTreesDTOs);
    }

    @Override
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
    public ResponseEntity<ProductDTO> getProductById(UUID productId) {
        Product product = productService.findById(productId);
        return ResponseEntity.ok(product.toDTO());
    }

    @Override
    public ResponseEntity<List<ProductDTO>> getProductChildren(UUID productId) {
        List<Product> products = productService.findChildren(productId);
        List<ProductDTO> productDTOs = products.stream().map(Product::toDTO).toList();
        return ResponseEntity.ok(productDTOs);
    }

    @Override
    public ResponseEntity<ProductTreeDTO> getProductTree(UUID productId, Integer depth) {
        ProductTree productTree = productService.findChildrenTree(productId, depth);
        return ResponseEntity.ok(productTree.toDTO());
    }

    @Override
    public ResponseEntity<List<ProductDTO>> getProductsRoots() {
        List<Product> products = productService.findRootProducts();
        List<ProductDTO> productDTOs = products.stream().map(Product::toDTO).toList();
        return ResponseEntity.ok(productDTOs);
    }

    @Override
    public ResponseEntity<ProductDTO> updateProduct(UUID productId, @Valid ProductDTO productDTO) {
        productDTO.setId(productId);
        Product product = Product.fromDTO(productDTO);
        Product updatedProduct = productService.update(product);
        return ResponseEntity.ok(updatedProduct.toDTO());
    }
}
