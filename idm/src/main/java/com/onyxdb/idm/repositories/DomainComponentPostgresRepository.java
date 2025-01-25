package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.DomainComponentTable;
import com.onyxdb.idm.models.DomainComponent;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DomainComponentPostgresRepository implements DomainComponentRepository {

    private final DSLContext dslContext;
    private final DomainComponentTable domainComponentTable = Tables.DOMAIN_COMPONENT_TABLE;

    @Override
    public Optional<DomainComponent> findById(UUID id) {
        return dslContext.selectFrom(domainComponentTable)
                .where(domainComponentTable.ID.eq(id))
                .fetchOptional(record -> DomainComponent.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .description(record.getDescription())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .build());
    }

    @Override
    public List<DomainComponent> findAll() {
        return dslContext.selectFrom(domainComponentTable)
                .fetch(record -> DomainComponent.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .description(record.getDescription())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .build());
    }

    @Override
    public void create(DomainComponent domainComponent) {
        dslContext.insertInto(domainComponentTable)
                .set(domainComponentTable.ID, domainComponent.getId())
                .set(domainComponentTable.NAME, domainComponent.getName())
                .set(domainComponentTable.DESCRIPTION, domainComponent.getDescription())
                .set(domainComponentTable.CREATED_AT, domainComponent.getCreatedAt())
                .set(domainComponentTable.UPDATED_AT, domainComponent.getUpdatedAt())
                .execute();
    }

    @Override
    public void update(DomainComponent domainComponent) {
        dslContext.update(domainComponentTable)
                .set(domainComponentTable.NAME, domainComponent.getName())
                .set(domainComponentTable.DESCRIPTION, domainComponent.getDescription())
                .set(domainComponentTable.UPDATED_AT, domainComponent.getUpdatedAt())
                .where(domainComponentTable.ID.eq(domainComponent.getId()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(domainComponentTable)
                .where(domainComponentTable.ID.eq(id))
                .execute();
    }
}