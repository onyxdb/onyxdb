package com.onyxdb.idm.models;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.JSONB;

import com.onyxdb.idm.generated.jooq.tables.records.BusinessRoleTableRecord;
import com.onyxdb.idm.generated.openapi.models.BusinessRoleDTO;

/**
 * @author ArtemFed
 */
public record BusinessRole(
        UUID id,
        String name,
        String description,
        UUID parentId,
        Map<String, Object> data,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    static final TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
    };
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static BusinessRole fromDTO(BusinessRoleDTO businessRoleDTO) {
        return new BusinessRole(
                businessRoleDTO.getId(),
                businessRoleDTO.getName(),
                businessRoleDTO.getDescription(),
                businessRoleDTO.getParentId(),
                businessRoleDTO.getData(),
                businessRoleDTO.getCreatedAt(),
                businessRoleDTO.getUpdatedAt()
        );
    }

    public static BusinessRole fromDAO(BusinessRoleTableRecord businessRoleDAO) {
        Map<String, Object> dataMap = null;
        try {
            dataMap = objectMapper.readValue(businessRoleDAO.getData().data(), typeRef);
        } catch (JsonProcessingException ignored) {
        }
        return new BusinessRole(
                businessRoleDAO.getId(),
                businessRoleDAO.getName(),
                businessRoleDAO.getDescription(),
                businessRoleDAO.getParentId(),
                dataMap,
                businessRoleDAO.getCreatedAt(),
                businessRoleDAO.getUpdatedAt()
        );
    }

    public BusinessRoleDTO toDTO() {
        return new BusinessRoleDTO()
                .id(id)
                .name(name)
                .description(description)
                .parentId(parentId)
                .data(data)
                .createdAt(createdAt)
                .updatedAt(updatedAt);
    }

    public JSONB getDataAsJsonb() {
        try {
            return JSONB.valueOf(objectMapper.writeValueAsString(data));
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert Map to JSONB", e);
        }
    }
}