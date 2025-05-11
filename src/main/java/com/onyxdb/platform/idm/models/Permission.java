package com.onyxdb.platform.idm.models;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.JSONB;

import com.onyxdb.platform.generated.jooq.tables.records.PermissionTableRecord;
import com.onyxdb.platform.generated.openapi.models.PermissionPostDTO;
import com.onyxdb.platform.generated.openapi.models.PermissionDTO;

/**
 * @author ArtemFed
 */
public record Permission(
        UUID id,
        String actionType,
        String resourceType,
        Map<String, Object> data,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    static final TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
    };
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Permission fromDTO(PermissionDTO permissionDTO) {
        return new Permission(
                permissionDTO.getId(),
                permissionDTO.getActionType(),
                permissionDTO.getResourceType(),
                permissionDTO.getData(),
                permissionDTO.getCreatedAt(),
                permissionDTO.getUpdatedAt()
        );
    }

    public static Permission fromPostDTO(PermissionPostDTO permissionDTO) {
        return new Permission(
                null,
                permissionDTO.getActionType(),
                permissionDTO.getResourceType(),
                permissionDTO.getData(),
                null,
                null
        );
    }

    public static Permission fromDAO(PermissionTableRecord permissionDAO) {
        Map<String, Object> dataMap = null;
        try {
            dataMap = objectMapper.readValue(permissionDAO.getData().data(), typeRef);
        } catch (JsonProcessingException ignored) {
        }

        return new Permission(
                permissionDAO.getId(),
                permissionDAO.getActionType(),
                permissionDAO.getResourceType(),
                dataMap == null || dataMap.isEmpty() ? null : dataMap,
                permissionDAO.getCreatedAt(),
                permissionDAO.getUpdatedAt()
        );
    }

    public PermissionDTO toDTO() {
        return new PermissionDTO()
                .id(id)
                .actionType(actionType)
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