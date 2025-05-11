package com.onyxdb.platform.idm.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.platform.generated.jooq.tables.records.RoleTableRecord;
import com.onyxdb.platform.generated.openapi.models.RolePostDTO;
import com.onyxdb.platform.generated.openapi.models.RoleDTO;

/**
 * @author ArtemFed
 */
public record Role(
        UUID id,
        String roleType,
        String name,
        String shopName,
        String description,
        Boolean isShopHidden,
        String entity,
        UUID orgUnitId,
        UUID productId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static Role fromDTO(RoleDTO roleDTO) {
        return new Role(
                roleDTO.getId(),
                roleDTO.getRoleType(),
                roleDTO.getName(),
                roleDTO.getShopName(),
                roleDTO.getDescription(),
                roleDTO.getIsShopHidden(),
                roleDTO.getEntity(),
                roleDTO.getOrgUnitId(),
                roleDTO.getProductId(),
                roleDTO.getCreatedAt(),
                roleDTO.getUpdatedAt()
        );
    }

    public static Role fromPostDTO(RolePostDTO roleDTO) {
        return new Role(
                roleDTO.getId(),
                roleDTO.getRoleType(),
                roleDTO.getName(),
                roleDTO.getShopName(),
                roleDTO.getDescription(),
                roleDTO.getIsShopHidden(),
                roleDTO.getEntity(),
                roleDTO.getOrgUnitId(),
                roleDTO.getProductId(),
                null,
                null
        );
    }

    public static Role fromDAO(RoleTableRecord roleDAO) {
        return new Role(
                roleDAO.getId(),
                roleDAO.getRoleType(),
                roleDAO.getName(),
                roleDAO.getShopName(),
                roleDAO.getDescription(),
                roleDAO.getIsShopHidden(),
                roleDAO.getEntity(),
                roleDAO.getOrgUnitId(),
                roleDAO.getProductId(),
                roleDAO.getCreatedAt(),
                roleDAO.getUpdatedAt()
        );
    }

    public RoleDTO toDTO() {
        return new RoleDTO()
                .id(id)
                .roleType(roleType)
                .name(name)
                .shopName(shopName)
                .description(description)
                .isShopHidden(isShopHidden)
                .entity(entity)
                .productId(productId)
                .orgUnitId(orgUnitId)
                .createdAt(createdAt)
                .updatedAt(updatedAt);
    }
}