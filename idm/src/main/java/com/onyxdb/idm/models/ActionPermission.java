package com.onyxdb.idm.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.onyxdb.idm.generated.jooq.tables.records.ActionPermissionTableRecord;
import com.onyxdb.idm.generated.openapi.models.ActionPermissionDTO;

/**
 * @author ArtemFed
 */
public record ActionPermission(
        UUID id,
        String actionType,
        List<String> resourceFields,
        List<String> labels,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public ActionPermissionDTO toDTO() {
        return new ActionPermissionDTO()
                .id(id)
                .actionType(actionType)
                .resourceFields(resourceFields)
                .createdAt(createdAt)
                .updatedAt(updatedAt);
    }

    public static ActionPermission fromDTO(ActionPermissionDTO permissionDTO) {
        return new ActionPermission(
                permissionDTO.getId(),
                permissionDTO.getActionType(),
                permissionDTO.getResourceFields(),
                permissionDTO.getLabels(),
                permissionDTO.getCreatedAt(),
                permissionDTO.getUpdatedAt()
        );
    }

    public static ActionPermission fromDAO(ActionPermissionTableRecord permissionDAO) {
        return new ActionPermission(
                permissionDAO.getId(),
                permissionDAO.getActionType(),
                List.of(permissionDAO.getResourceFields()),
                List.of(permissionDAO.getLabels()),
                permissionDAO.getCreatedAt(),
                permissionDAO.getUpdatedAt()
        );
    }
}