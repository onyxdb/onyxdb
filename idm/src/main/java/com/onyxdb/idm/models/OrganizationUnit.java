package com.onyxdb.idm.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.idm.generated.jooq.tables.records.OrganizationUnitTableRecord;
import com.onyxdb.idm.generated.openapi.models.OrganizationUnitDTO;

/**
 * @author ArtemFed
 */
public record OrganizationUnit(
        UUID id,
        String name,
        String description,
        UUID domainComponentId,
        UUID parentId,
        UUID ownerId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static OrganizationUnit fromDTO(OrganizationUnitDTO organizationUnitDTO) {
        return new OrganizationUnit(
                organizationUnitDTO.getId(),
                organizationUnitDTO.getName(),
                organizationUnitDTO.getDescription(),
                organizationUnitDTO.getDomainComponentId(),
                organizationUnitDTO.getParentId(),
                organizationUnitDTO.getOwnerId(),
                organizationUnitDTO.getCreatedAt(),
                organizationUnitDTO.getUpdatedAt()
        );
    }

    public static OrganizationUnit fromDAO(OrganizationUnitTableRecord organizationUnitDAO) {
        return new OrganizationUnit(
                organizationUnitDAO.getId(),
                organizationUnitDAO.getName(),
                organizationUnitDAO.getDescription(),
                organizationUnitDAO.getDomainComponentId(),
                organizationUnitDAO.getParentId(),
                organizationUnitDAO.getOwnerId(),
                organizationUnitDAO.getCreatedAt(),
                organizationUnitDAO.getUpdatedAt()
        );
    }

    public OrganizationUnitDTO toDTO() {
        return new OrganizationUnitDTO()
                .id(id)
                .name(name)
                .description(description)
                .domainComponentId(domainComponentId)
                .parentId(parentId)
                .ownerId(ownerId)
                .createdAt(createdAt)
                .updatedAt(updatedAt);
    }
}
