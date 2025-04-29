package com.onyxdb.platform.idm.models;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.JSONB;

import com.onyxdb.platform.generated.jooq.tables.records.ProductTableRecord;
import com.onyxdb.platform.generated.openapi.models.ProductDTO;
import com.onyxdb.platform.generated.openapi.models.ProductDTOGet;

/**
 * @author ArtemFed
 */
public record Product(
        UUID id,
        String name,
        String description,
        UUID parent_id,
        UUID ownerId,
        Map<String, Object> data,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    static final TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
    };
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Product fromDTO(ProductDTO productDTO) {
        return new Product(
                productDTO.getId(),
                productDTO.getName(),
                productDTO.getDescription(),
                productDTO.getParentId(),
                productDTO.getOwnerId(),
                productDTO.getData(),
                productDTO.getCreatedAt(),
                productDTO.getUpdatedAt()
        );
    }

    public static Product fromDAO(ProductTableRecord productDAO) {
        Map<String, Object> dataMap = null;
        try {
            dataMap = objectMapper.readValue(productDAO.getData().data(), typeRef);
        } catch (JsonProcessingException ignored) {
        }
        return new Product(
                productDAO.getId(),
                productDAO.getName(),
                productDAO.getDescription(),
                productDAO.getParentId(),
                productDAO.getOwnerId(),
                dataMap,
                productDAO.getCreatedAt(),
                productDAO.getUpdatedAt()
        );
    }

    public ProductDTOGet toDTO() {
        return new ProductDTOGet()
                .id(id)
                .name(name)
                .description(description)
                .parentId(parent_id)
                .ownerId(ownerId)
                .data(data)
                .createdAt(createdAt)
                .updatedAt(updatedAt);
    }

    public ProductTableRecord toDAO() {
        return new ProductTableRecord(
                id,
                name,
                description,
                parent_id,
                ownerId,
                getDataAsJsonb(),
                createdAt,
                updatedAt
        );
    }

    public JSONB getDataAsJsonb() {
        try {
            return JSONB.valueOf(objectMapper.writeValueAsString(data));
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert Map to JSONB", e);
        }
    }
}