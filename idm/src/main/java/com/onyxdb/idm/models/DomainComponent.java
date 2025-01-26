package com.onyxdb.idm.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.idm.generated.jooq.tables.records.DomainComponentTableRecord;
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

    public static DomainComponent fromDAO(DomainComponentTableRecord domainComponentDAO) {
        return new DomainComponent(
                domainComponentDAO.getId(),
                domainComponentDAO.getName(),
                domainComponentDAO.getDescription(),
                domainComponentDAO.getCreatedAt(),
                domainComponentDAO.getUpdatedAt()
        );
    }
}