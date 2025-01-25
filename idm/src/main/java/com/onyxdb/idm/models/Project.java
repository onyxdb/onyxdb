package com.onyxdb.idm.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
import com.onyxdb.idm.generated.openapi.models.ProjectDTO;

@Data
@Builder
public class Project {
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID resourceId;
    private UUID organizationId;
    private UUID ownerId;
    private UUID responsibleId;

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
        return Project.builder()
                .id(projectDTO.getId())
                .name(projectDTO.getName())
                .description(projectDTO.getDescription())
                .createdAt(projectDTO.getCreatedAt())
                .updatedAt(projectDTO.getUpdatedAt())
                .resourceId(projectDTO.getResourceId())
                .organizationId(projectDTO.getOrganizationId())
                .ownerId(projectDTO.getOwnerId())
                .build();
    }
}