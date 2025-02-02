package com.onyxdb.idm.controllers.v1;

import com.onyxdb.idm.generated.openapi.apis.ProductsApi;
import com.onyxdb.idm.generated.openapi.models.ProductDTO;
import com.onyxdb.idm.models.Product;
import com.onyxdb.idm.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<ProductDTO> getProductById(UUID productId) {
        Product product = productService.findById(productId);
        return ResponseEntity.ok(product.toDTO());
    }

    @Override
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productService.findAll();
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
