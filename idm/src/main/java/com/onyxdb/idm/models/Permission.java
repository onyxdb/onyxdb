package com.onyxdb.idm.models;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.JSONB;

import com.onyxdb.idm.generated.jooq.tables.records.PermissionTableRecord;
import com.onyxdb.idm.generated.openapi.models.PermissionDTO;

/**
 * @author ArtemFed
 */
public record Permission(
        UUID id,
        String actionType,
        Map<String, Object> data,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    static final TypeReference<Map<String, Object>> typeRef = new TypeReference<>(){};

    public PermissionDTO toDTO() {
        return new PermissionDTO()
                .id(id)
                .actionType(actionType)
                .data(data)
                .createdAt(createdAt)
                .updatedAt(updatedAt);
    }

    public static Permission fromDTO(PermissionDTO permissionDTO) {
        return new Permission(
                permissionDTO.getId(),
                permissionDTO.getActionType(),
                permissionDTO.getData(),
                permissionDTO.getCreatedAt(),
                permissionDTO.getUpdatedAt()
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
                dataMap,
                permissionDAO.getCreatedAt(),
                permissionDAO.getUpdatedAt()
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