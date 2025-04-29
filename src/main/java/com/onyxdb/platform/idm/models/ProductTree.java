package com.onyxdb.platform.idm.models;

import java.util.ArrayList;
import java.util.List;

import com.onyxdb.platform.generated.openapi.models.ProductTreeDTO;

/**
 * @author ArtemFed
 */
public record ProductTree(
        Product product,
        List<ProductTree> children
) {
    public ProductTreeDTO toDTO() {
        return new ProductTreeDTO()
                .item(product.toDTO())
                .children(children.isEmpty() ? new ArrayList<>() : children.stream().map(ProductTree::toDTO).toList());
    }
}