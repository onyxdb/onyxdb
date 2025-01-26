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

/**
 * @author ArtemFed
 */
@Repository
@RequiredArgsConstructor
public class OrganizationPostgresRepository implements OrganizationRepository {

    private final DSLContext dslContext;
    private final OrganizationTable organizationTable = Tables.ORGANIZATION_TABLE;

    @Override
    public Optional<Organization> findById(UUID id) {
        return dslContext.selectFrom(organizationTable)
                .where(organizationTable.ID.eq(id))
                .fetchOptional(Organization::fromDAO);
    }

    @Override
    public List<Organization> findAll() {
        return dslContext.selectFrom(organizationTable)
                .fetch(Organization::fromDAO);
    }

    @Override
    public void create(Organization organization) {
        dslContext.insertInto(organizationTable)
                .set(organizationTable.ID, organization.id())
                .set(organizationTable.NAME, organization.name())
                .set(organizationTable.DESCRIPTION, organization.description())
                .set(organizationTable.CREATED_AT, organization.createdAt())
                .set(organizationTable.UPDATED_AT, organization.updatedAt())
                .set(organizationTable.RESOURCE_ID, organization.resourceId())
                .set(organizationTable.OWNER_ID, organization.ownerId())
                .execute();
    }

    @Override
    public void update(Organization organization) {
        dslContext.update(organizationTable)
                .set(organizationTable.NAME, organization.name())
                .set(organizationTable.DESCRIPTION, organization.description())
                .set(organizationTable.UPDATED_AT, organization.updatedAt())
                .set(organizationTable.RESOURCE_ID, organization.resourceId())
                .set(organizationTable.OWNER_ID, organization.ownerId())
                .where(organizationTable.ID.eq(organization.id()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(organizationTable)
                .where(organizationTable.ID.eq(id))
                .execute();
    }
}