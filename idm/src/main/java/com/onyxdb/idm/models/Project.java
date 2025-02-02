package com.onyxdb.idm.models;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.JSONB;

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
        Map<String, Object> data,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    private static final ObjectMapper objectMapper = new ObjectMapper();

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
                getDataAsJsonb(),
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
                projectDTO.getData(),
                projectDTO.getCreatedAt(),
                projectDTO.getUpdatedAt()
        );
    }

    public static Project fromDAO(ProjectTableRecord projectDAO) {
        Map<String, Object> dataMap = null;
        try {
            dataMap = objectMapper.readValue(
                    projectDAO.getData().data(),
                    new TypeReference<Map<String, Object>>() {
                    }
            );
        } catch (JsonProcessingException ignored) {
        }
        return new Project(
                projectDAO.getId(),
                projectDAO.getName(),
                projectDAO.getDescription(),
                projectDAO.getParentId(),
                projectDAO.getOwnerId(),
                dataMap,
                projectDAO.getCreatedAt(),
                projectDAO.getUpdatedAt()
        );
    }

    public JSONB getDataAsJsonb() {
        try {
            return JSONB.valueOf(objectMapper.writeValueAsString(data));
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert Map to JSONB", e);
        }
    }
}