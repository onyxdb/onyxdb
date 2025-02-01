package com.onyxdb.idm.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.idm.generated.jooq.tables.records.RoleTableRecord;
import com.onyxdb.idm.generated.openapi.models.RoleDTO;

/**
 * @author ArtemFed
 */
public record Role(
        UUID id,
        String role_type,
        String name,
        String shop_name,
        String description,
        UUID projectId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public RoleDTO toDTO() {
        return new RoleDTO()
                .id(id)
                .roleType(role_type)
                .name(name)
                .shopName(shop_name)
                .description(description)
                .projectId(projectId)
                .createdAt(createdAt)
                .updatedAt(updatedAt);
    }

    public static Role fromDTO(RoleDTO roleDTO) {
        return new Role(
                roleDTO.getId(),
                roleDTO.getRoleType(),
                roleDTO.getName(),
                roleDTO.getShopName(),
                roleDTO.getDescription(),
                roleDTO.getProjectId(),
                roleDTO.getCreatedAt(),
                roleDTO.getUpdatedAt()
        );
    }

    public static Role fromDAO(RoleTableRecord roleDAO) {
        return new Role(
                roleDAO.getId(),
                roleDAO.getRoleType(),
                roleDAO.getName(),
                roleDAO.getShopName(),
                roleDAO.getDescription(),
                roleDAO.getProjectId(),
                roleDAO.getCreatedAt(),
                roleDAO.getUpdatedAt()
        );
    }
}