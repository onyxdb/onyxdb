package com.onyxdb.idm.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.idm.generated.openapi.models.DomainComponentDTO;

public record DomainComponent(
        UUID id,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public DomainComponentDTO toDTO() {
        return new DomainComponentDTO()
                .id(id)
                .name(name)
                .description(description)
                .createdAt(createdAt)
                .updatedAt(updatedAt);
    }

    public static DomainComponent fromDTO(DomainComponentDTO domainComponentDTO) {
        return new DomainComponent(
                domainComponentDTO.getId(),
                domainComponentDTO.getName(),
                domainComponentDTO.getDescription(),
                domainComponentDTO.getCreatedAt(),
                domainComponentDTO.getUpdatedAt()
        );
    }
}