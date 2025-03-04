package com.onyxdb.idm.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.JSONB;

import com.onyxdb.idm.generated.jooq.tables.records.ProductTableRecord;
import com.onyxdb.idm.generated.openapi.models.ProductDTO;
import com.onyxdb.idm.generated.openapi.models.ProductTreeDTO;
import com.onyxdb.idm.generated.openapi.models.RoleDTO;

/**
 * @author ArtemFed
 */
public record ProductTree(
        Product product,
        List<ProductTree> children
) {
    public ProductTreeDTO toDTO() {
        return new ProductTreeDTO()
                .product(product.toDTO())
                .children(children.isEmpty() ? new ArrayList<>() : children.stream().map(ProductTree::toDTO).toList());
    }
}