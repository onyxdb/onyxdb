package com.onyxdb.idm.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.idm.generated.openapi.models.OrganizationUnitDTO;

public record OrganizationUnit(
        UUID id,
        String name,
        String description,
        UUID domainComponentId,
        UUID parentId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public OrganizationUnitDTO toDTO() {
        return new OrganizationUnitDTO()
                .id(id)
                .name(name)
                .description(description)
                .domainComponentId(domainComponentId)
                .parentId(parentId)
                .createdAt(createdAt)
                .updatedAt(updatedAt);
    }

    public static OrganizationUnit fromDTO(OrganizationUnitDTO organizationUnitDTO) {
        return new OrganizationUnit(
                organizationUnitDTO.getId(),
                organizationUnitDTO.getName(),
                organizationUnitDTO.getDescription(),
                organizationUnitDTO.getDomainComponentId(),
                organizationUnitDTO.getParentId(),
                organizationUnitDTO.getCreatedAt(),
                organizationUnitDTO.getUpdatedAt()
        );
    }
}