package com.onyxdb.idm.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.idm.generated.jooq.tables.records.ApiPermissionTableRecord;
import com.onyxdb.idm.generated.openapi.models.ApiPermissionDTO;

/**
 * @author ArtemFed
 */
public record ApiPermission(
        UUID id,
        String apiPathRegexp,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public ApiPermissionDTO toDTO() {
        return new ApiPermissionDTO()
                .id(id)
                .createdAt(createdAt)
                .updatedAt(updatedAt);
    }

    public static ApiPermission fromDTO(ApiPermissionDTO permissionDTO) {
        return new ApiPermission(
                permissionDTO.getId(),
                permissionDTO.getApiPathRegexp(),
                permissionDTO.getCreatedAt(),
                permissionDTO.getUpdatedAt()
        );
    }

    public static ApiPermission fromDAO(ApiPermissionTableRecord permissionDAO) {
        return new ApiPermission(
                permissionDAO.getId(),
                permissionDAO.getApiPathRegexp(),
                permissionDAO.getCreatedAt(),
                permissionDAO.getUpdatedAt()
        );
    }
}