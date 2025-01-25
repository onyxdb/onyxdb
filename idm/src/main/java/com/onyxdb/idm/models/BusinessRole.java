package com.onyxdb.idm.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.idm.generated.openapi.models.BusinessRoleDTO;

public record BusinessRole(
        UUID id,
        String name,
        String description,
        UUID parentId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public BusinessRoleDTO toDTO() {
        return new BusinessRoleDTO()
                .id(id)
                .name(name)
                .description(description)
                .parentId(parentId)
                .createdAt(createdAt)
                .updatedAt(updatedAt);
    }

    public static BusinessRole fromDTO(BusinessRoleDTO businessRoleDTO) {
        return new BusinessRole(
                businessRoleDTO.getId(),
                businessRoleDTO.getName(),
                businessRoleDTO.getDescription(),
                businessRoleDTO.getParentId(),
                businessRoleDTO.getCreatedAt(),
                businessRoleDTO.getUpdatedAt()
        );
    }
}