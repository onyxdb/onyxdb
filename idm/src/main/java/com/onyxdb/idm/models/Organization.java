package com.onyxdb.idm.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.idm.generated.openapi.models.OrganizationDTO;

public record Organization(
        UUID id,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UUID resourceId,
        UUID accountId,
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
                .accountId(accountId)
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
                organizationDTO.getAccountId(),
                organizationDTO.getOwnerId()
        );
    }
}