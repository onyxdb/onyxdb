package com.onyxdb.idm.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.idm.generated.openapi.models.ServiceDTO;

public record Service(
        UUID id,
        String name,
        String type,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UUID resourceId,
        UUID projectId,
        UUID ownerId
) {
    public ServiceDTO toDTO() {
        return new ServiceDTO()
                .id(id)
                .name(name)
                .type(type)
                .description(description)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .resourceId(resourceId)
                .projectId(projectId)
                .ownerId(ownerId);
    }

    public static Service fromDTO(ServiceDTO serviceDTO) {
        return new Service(
                serviceDTO.getId(),
                serviceDTO.getName(),
                serviceDTO.getType(),
                serviceDTO.getDescription(),
                serviceDTO.getCreatedAt(),
                serviceDTO.getUpdatedAt(),
                serviceDTO.getResourceId(),
                serviceDTO.getProjectId(),
                serviceDTO.getOwnerId()
        );
    }
}