package com.onyxdb.idm.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.idm.generated.jooq.tables.records.ProjectTableRecord;
import com.onyxdb.idm.generated.openapi.models.ProjectDTO;

/**
 * @author ArtemFed
 */
public record Project(
        UUID id,
        String name,
        String description,
        UUID parent_id,
        UUID ownerId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public ProjectDTO toDTO() {
        return new ProjectDTO()
                .id(id)
                .name(name)
                .description(description)
                .parentId(parent_id)
                .ownerId(ownerId)
                .createdAt(createdAt)
                .updatedAt(updatedAt);
    }

    public ProjectTableRecord toDAO() {
        return new ProjectTableRecord(
                id,
                name,
                description,
                parent_id,
                ownerId,
                createdAt,
                updatedAt
        );
    }

    public static Project fromDTO(ProjectDTO projectDTO) {
        return new Project(
                projectDTO.getId(),
                projectDTO.getName(),
                projectDTO.getDescription(),
                projectDTO.getParentId(),
                projectDTO.getOwnerId(),
                projectDTO.getCreatedAt(),
                projectDTO.getUpdatedAt()
        );
    }

    public static Project fromDAO(ProjectTableRecord projectDAO) {
        return new Project(
                projectDAO.getId(),
                projectDAO.getName(),
                projectDAO.getDescription(),
                projectDAO.getParentId(),
                projectDAO.getOwnerId(),
                projectDAO.getCreatedAt(),
                projectDAO.getUpdatedAt()
        );
    }
}