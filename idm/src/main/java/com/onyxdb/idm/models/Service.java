package com.onyxdb.idm.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
import com.onyxdb.idm.generated.openapi.models.ServiceDTO;

@Data
@Builder
public class Service {
    private UUID id;
    private String name;
    private String type;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID resourceId;
    private UUID projectId;
    private UUID ownerId;

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
        return Service.builder()
                .id(serviceDTO.getId())
                .name(serviceDTO.getName())
                .type(serviceDTO.getType())
                .description(serviceDTO.getDescription())
                .createdAt(serviceDTO.getCreatedAt())
                .updatedAt(serviceDTO.getUpdatedAt())
                .resourceId(serviceDTO.getResourceId())
                .projectId(serviceDTO.getProjectId())
                .ownerId(serviceDTO.getOwnerId())
                .build();
    }
}