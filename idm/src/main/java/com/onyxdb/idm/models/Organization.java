package com.onyxdb.idm.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.idm.generated.jooq.tables.records.OrganizationTableRecord;
import com.onyxdb.idm.generated.openapi.models.OrganizationDTO;

/**
 * @author ArtemFed
 */
public record Organization(
        UUID id,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UUID resourceId,
        UUID ownerId
) {
    public OrganizationDTO toDTO() {
        return new OrganizationDTO()
                .id(id)
                .name(name)
                .description(description)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .resourceId(resourceId)
                .ownerId(ownerId);
    }

    public static Organization fromDTO(OrganizationDTO organizationDTO) {
        return new Organization(
                organizationDTO.getId(),
                organizationDTO.getName(),
                organizationDTO.getDescription(),
                organizationDTO.getCreatedAt(),
                organizationDTO.getUpdatedAt(),
                organizationDTO.getResourceId(),
                organizationDTO.getOwnerId()
        );
    }

    public static Organization fromDAO(OrganizationTableRecord organizationDAO) {
        return new Organization(
                organizationDAO.getId(),
                organizationDAO.getName(),
                organizationDAO.getDescription(),
                organizationDAO.getCreatedAt(),
                organizationDAO.getUpdatedAt(),
                organizationDAO.getResourceId(),
                organizationDAO.getOwnerId()
        );
    }
}
