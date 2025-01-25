package com.onyxdb.idm.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
import com.onyxdb.idm.generated.openapi.models.OrganizationDTO;

@Data
@Builder
public class Organization {
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID resourceId;
    private UUID accountId;
    private UUID ownerId;
    private UUID responsibleId;
    private String[] participants;

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
        return Organization.builder()
                .id(organizationDTO.getId())
                .name(organizationDTO.getName())
                .description(organizationDTO.getDescription())
                .createdAt(organizationDTO.getCreatedAt())
                .updatedAt(organizationDTO.getUpdatedAt())
                .resourceId(organizationDTO.getResourceId())
                .accountId(organizationDTO.getAccountId())
                .ownerId(organizationDTO.getOwnerId())
                .build();
    }
}