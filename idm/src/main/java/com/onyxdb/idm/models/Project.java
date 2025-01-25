package com.onyxdb.idm.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.idm.generated.openapi.models.ProjectDTO;

public record Project(
        UUID id,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UUID resourceId,
        UUID organizationId,
        UUID ownerId
) {
    public ProjectDTO toDTO() {
        return new ProjectDTO()
                .id(id)
                .name(name)
                .description(description)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .resourceId(resourceId)
                .organizationId(organizationId)
                .ownerId(ownerId);
    }

    public static Project fromDTO(ProjectDTO projectDTO) {
        return new Project(
                projectDTO.getId(),
                projectDTO.getName(),
                projectDTO.getDescription(),
                projectDTO.getCreatedAt(),
                projectDTO.getUpdatedAt(),
                projectDTO.getResourceId(),
                projectDTO.getOrganizationId(),
                projectDTO.getOwnerId()
        );
    }
}