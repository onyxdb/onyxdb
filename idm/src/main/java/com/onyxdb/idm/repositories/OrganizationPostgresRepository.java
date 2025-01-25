package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.OrganizationTable;
import com.onyxdb.idm.models.Organization;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrganizationPostgresRepository implements OrganizationRepository {

    private final DSLContext dslContext;
    private final OrganizationTable organizationTable = Tables.ORGANIZATION_TABLE;

    @Override
    public Optional<Organization> findById(UUID id) {
        return dslContext.selectFrom(organizationTable)
                .where(organizationTable.ID.eq(id))
                .fetchOptional(record -> Organization.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .description(record.getDescription())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .resourceId(record.getResourceId())
                        .ownerId(record.getOwnerId())
                        .build());
    }

    @Override
    public List<Organization> findAll() {
        return dslContext.selectFrom(organizationTable)
                .fetch(record -> Organization.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .description(record.getDescription())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .resourceId(record.getResourceId())
                        .ownerId(record.getOwnerId())
                        .build());
    }

    @Override
    public void create(Organization organization) {
        dslContext.insertInto(organizationTable)
                .set(organizationTable.ID, organization.getId())
                .set(organizationTable.NAME, organization.getName())
                .set(organizationTable.DESCRIPTION, organization.getDescription())
                .set(organizationTable.CREATED_AT, organization.getCreatedAt())
                .set(organizationTable.UPDATED_AT, organization.getUpdatedAt())
                .set(organizationTable.RESOURCE_ID, organization.getResourceId())
                .execute();
    }

    @Override
    public void update(Organization organization) {
        dslContext.update(organizationTable)
                .set(organizationTable.NAME, organization.getName())
                .set(organizationTable.DESCRIPTION, organization.getDescription())
                .set(organizationTable.UPDATED_AT, organization.getUpdatedAt())
                .set(organizationTable.RESOURCE_ID, organization.getResourceId())
                .set(organizationTable.OWNER_ID, organization.getOwnerId())
                .where(organizationTable.ID.eq(organization.getId()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(organizationTable)
                .where(organizationTable.ID.eq(id))
                .execute();
    }
}