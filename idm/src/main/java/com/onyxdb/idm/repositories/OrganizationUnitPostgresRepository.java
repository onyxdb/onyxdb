package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.OrganizationUnitTable;
import com.onyxdb.idm.models.OrganizationUnit;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrganizationUnitPostgresRepository implements OrganizationUnitRepository {

    private final DSLContext dslContext;
    private final OrganizationUnitTable organizationUnitTable = Tables.ORGANIZATION_UNIT_TABLE;

    @Override
    public Optional<OrganizationUnit> findById(UUID id) {
        return dslContext.selectFrom(organizationUnitTable)
                .where(organizationUnitTable.ID.eq(id))
                .fetchOptional(record -> new OrganizationUnit(
                        record.getId(),
                        record.getName(),
                        record.getDescription(),
                        record.getDomainComponentId(),
                        record.getParentId(),
                        record.getCreatedAt(),
                        record.getUpdatedAt()
                ));
    }

    @Override
    public List<OrganizationUnit> findAll() {
        return dslContext.selectFrom(organizationUnitTable)
                .fetch(record -> new OrganizationUnit(
                        record.getId(),
                        record.getName(),
                        record.getDescription(),
                        record.getDomainComponentId(),
                        record.getParentId(),
                        record.getCreatedAt(),
                        record.getUpdatedAt()
                ));
    }

    @Override
    public List<OrganizationUnit> findByDomainComponentId(UUID domainComponentId) {
        return dslContext.selectFrom(organizationUnitTable)
                .where(organizationUnitTable.DOMAIN_COMPONENT_ID.eq(domainComponentId))
                .fetch(record ->  new OrganizationUnit(
                        record.getId(),
                        record.getName(),
                        record.getDescription(),
                        record.getDomainComponentId(),
                        record.getParentId(),
                        record.getCreatedAt(),
                        record.getUpdatedAt()
                ));
    }

    @Override
    public void create(OrganizationUnit organizationUnit) {
        dslContext.insertInto(organizationUnitTable)
                .set(organizationUnitTable.ID, organizationUnit.id())
                .set(organizationUnitTable.NAME, organizationUnit.name())
                .set(organizationUnitTable.DESCRIPTION, organizationUnit.description())
                .set(organizationUnitTable.DOMAIN_COMPONENT_ID, organizationUnit.domainComponentId())
                .set(organizationUnitTable.PARENT_ID, organizationUnit.parentId())
                .set(organizationUnitTable.CREATED_AT, organizationUnit.createdAt())
                .set(organizationUnitTable.UPDATED_AT, organizationUnit.updatedAt())
                .execute();
    }

    @Override
    public void update(OrganizationUnit organizationUnit) {
        dslContext.update(organizationUnitTable)
                .set(organizationUnitTable.NAME, organizationUnit.name())
                .set(organizationUnitTable.DESCRIPTION, organizationUnit.description())
                .set(organizationUnitTable.DOMAIN_COMPONENT_ID, organizationUnit.domainComponentId())
                .set(organizationUnitTable.PARENT_ID, organizationUnit.parentId())
                .set(organizationUnitTable.UPDATED_AT, organizationUnit.updatedAt())
                .where(organizationUnitTable.ID.eq(organizationUnit.id()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(organizationUnitTable)
                .where(organizationUnitTable.ID.eq(id))
                .execute();
    }
}