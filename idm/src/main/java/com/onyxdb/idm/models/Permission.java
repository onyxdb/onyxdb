package com.onyxdb.idm.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.onyxdb.idm.generated.jooq.tables.records.PermissionTableRecord;
import com.onyxdb.idm.generated.openapi.models.PermissionDTO;

/**
 * @author ArtemFed
 */
public record Permission(
        UUID id,
        String actionType,
        String resourceType,
        List<String> resourceFields,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public PermissionDTO toDTO() {
        return new PermissionDTO()
                .id(id)
                .actionType(actionType)
                .resourceType(resourceType)
                .resourceFields(resourceFields)
                .createdAt(createdAt)
                .updatedAt(updatedAt);
    }

    public static Permission fromDTO(PermissionDTO permissionDTO) {
        return new Permission(
                permissionDTO.getId(),
                permissionDTO.getActionType(),
                permissionDTO.getResourceType(),
                permissionDTO.getResourceFields(),
                permissionDTO.getCreatedAt(),
                permissionDTO.getUpdatedAt()
        );
    }

    public static Permission fromDAO(PermissionTableRecord permissionDAO) {
        return new Permission(
                permissionDAO.getId(),
                permissionDAO.getActionType(),
                permissionDAO.getResourceType(),
                List.of(permissionDAO.getResourceFields()),
                permissionDAO.getCreatedAt(),
                permissionDAO.getUpdatedAt()
        );
    }
}