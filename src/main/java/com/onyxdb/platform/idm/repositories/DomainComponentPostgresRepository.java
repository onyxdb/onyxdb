package com.onyxdb.platform.idm.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.onyxdb.platform.generated.jooq.Tables;
import com.onyxdb.platform.generated.jooq.tables.DomainComponentTable;
import com.onyxdb.platform.idm.models.DomainComponent;

/**
 * @author ArtemFed
 */
@Repository
@RequiredArgsConstructor
public class DomainComponentPostgresRepository implements DomainComponentRepository {
    private final static DomainComponentTable domainComponentTable = Tables.DOMAIN_COMPONENT_TABLE;
    private final DSLContext dslContext;

    @Override
    public Optional<DomainComponent> findById(UUID id) {
        return dslContext.selectFrom(domainComponentTable)
                .where(domainComponentTable.ID.eq(id))
                .fetchOptional(DomainComponent::fromDAO);
    }

    @Override
    public List<DomainComponent> findAll() {
        return dslContext.selectFrom(domainComponentTable)
                .orderBy(domainComponentTable.CREATED_AT)
                .fetch(DomainComponent::fromDAO);
    }

    @Override
    public DomainComponent create(DomainComponent domainComponent) {
        var record = dslContext.insertInto(domainComponentTable)
                .set(domainComponentTable.ID, UUID.randomUUID())
                .set(domainComponentTable.NAME, domainComponent.name())
                .set(domainComponentTable.DESCRIPTION, domainComponent.description())
                .set(domainComponentTable.CREATED_AT, LocalDateTime.now())
                .set(domainComponentTable.UPDATED_AT, LocalDateTime.now())
                .returning()
                .fetchOne();

        assert record != null;
        return DomainComponent.fromDAO(record);
    }

    @Override
    public DomainComponent update(DomainComponent domainComponent) {
        var record = dslContext.update(domainComponentTable)
                .set(domainComponentTable.NAME, domainComponent.name())
                .set(domainComponentTable.DESCRIPTION, domainComponent.description())
                .set(domainComponentTable.UPDATED_AT, LocalDateTime.now())
                .where(domainComponentTable.ID.eq(domainComponent.id()))
                .returning()
                .fetchOne();

        assert record != null;
        return DomainComponent.fromDAO(record);
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(domainComponentTable)
                .where(domainComponentTable.ID.eq(id))
                .execute();
    }
}