package com.onyxdb.idm.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.idm.generated.openapi.models.RoleDTO;

public record Role(
        UUID id,
        String name,
        String description,
        UUID resourceId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public RoleDTO toDTO() {
        return new RoleDTO()
                .id(id)
                .name(name)
                .description(description)
                .resourceId(resourceId)
                .createdAt(createdAt)
                .updatedAt(updatedAt);
    }

    public static Role fromDTO(RoleDTO roleDTO) {
        return new Role(
                roleDTO.getId(),
                roleDTO.getName(),
                roleDTO.getDescription(),
                roleDTO.getResourceId(),
                roleDTO.getCreatedAt(),
                roleDTO.getUpdatedAt()
        );
    }
}