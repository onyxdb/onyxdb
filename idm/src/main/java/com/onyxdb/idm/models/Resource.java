package com.onyxdb.idm.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.idm.generated.openapi.models.ResourceDTO;

public record Resource(
        UUID id,
        String resourceType,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public ResourceDTO toDTO() {
        return new ResourceDTO()
                .id(id)
                .resourceType(resourceType)
                .createdAt(createdAt)
                .updatedAt(updatedAt);
    }

    public static Resource fromDTO(ResourceDTO resourceDTO) {
        return new Resource(
                resourceDTO.getId(),
                resourceDTO.getResourceType(),
                resourceDTO.getCreatedAt(),
                resourceDTO.getUpdatedAt()
        );
    }
}