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
                .fetchOptional(DomainComponent::fromDAO);
    }

    @Override
    public List<DomainComponent> findAll() {
        return dslContext.selectFrom(domainComponentTable)
                .fetch(DomainComponent::fromDAO);
    }

    @Override
    public void create(DomainComponent domainComponent) {
        dslContext.insertInto(domainComponentTable)
                .set(domainComponentTable.ID, domainComponent.id())
                .set(domainComponentTable.NAME, domainComponent.name())
                .set(domainComponentTable.DESCRIPTION, domainComponent.description())
                .set(domainComponentTable.CREATED_AT, domainComponent.createdAt())
                .set(domainComponentTable.UPDATED_AT, domainComponent.updatedAt())
                .execute();
    }

    @Override
    public void update(DomainComponent domainComponent) {
        dslContext.update(domainComponentTable)
                .set(domainComponentTable.NAME, domainComponent.name())
                .set(domainComponentTable.DESCRIPTION, domainComponent.description())
                .set(domainComponentTable.UPDATED_AT, domainComponent.updatedAt())
                .where(domainComponentTable.ID.eq(domainComponent.id()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(domainComponentTable)
                .where(domainComponentTable.ID.eq(id))
                .execute();
    }
}