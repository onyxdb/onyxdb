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
                .fetchOptional(record -> OrganizationUnit.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .description(record.getDescription())
                        .domainComponentId(record.getDomainComponentId())
                        .parentId(record.getParentId())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .build());
    }

    @Override
    public List<OrganizationUnit> findAll() {
        return dslContext.selectFrom(organizationUnitTable)
                .fetch(record -> OrganizationUnit.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .description(record.getDescription())
                        .domainComponentId(record.getDomainComponentId())
                        .parentId(record.getParentId())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .build());
    }

    @Override
    public List<OrganizationUnit> findByDomainComponentId(UUID domainComponentId) {
        return dslContext.selectFrom(organizationUnitTable)
                .where(organizationUnitTable.DOMAIN_COMPONENT_ID.eq(domainComponentId))
                .fetch(record -> OrganizationUnit.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .description(record.getDescription())
                        .domainComponentId(record.getDomainComponentId())
                        .parentId(record.getParentId())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .build());
    }

    @Override
    public void create(OrganizationUnit organizationUnit) {
        dslContext.insertInto(organizationUnitTable)
                .set(organizationUnitTable.ID, organizationUnit.getId())
                .set(organizationUnitTable.NAME, organizationUnit.getName())
                .set(organizationUnitTable.DESCRIPTION, organizationUnit.getDescription())
                .set(organizationUnitTable.DOMAIN_COMPONENT_ID, organizationUnit.getDomainComponentId())
                .set(organizationUnitTable.PARENT_ID, organizationUnit.getParentId())
                .set(organizationUnitTable.CREATED_AT, organizationUnit.getCreatedAt())
                .set(organizationUnitTable.UPDATED_AT, organizationUnit.getUpdatedAt())
                .execute();
    }

    @Override
    public void update(OrganizationUnit organizationUnit) {
        dslContext.update(organizationUnitTable)
                .set(organizationUnitTable.NAME, organizationUnit.getName())
                .set(organizationUnitTable.DESCRIPTION, organizationUnit.getDescription())
                .set(organizationUnitTable.DOMAIN_COMPONENT_ID, organizationUnit.getDomainComponentId())
                .set(organizationUnitTable.PARENT_ID, organizationUnit.getParentId())
                .set(organizationUnitTable.UPDATED_AT, organizationUnit.getUpdatedAt())
                .where(organizationUnitTable.ID.eq(organizationUnit.getId()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(organizationUnitTable)
                .where(organizationUnitTable.ID.eq(id))
                .execute();
    }
}