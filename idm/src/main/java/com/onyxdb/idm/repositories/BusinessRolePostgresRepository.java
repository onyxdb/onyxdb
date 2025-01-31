package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.BusinessRoleRoleTable;
import com.onyxdb.idm.generated.jooq.tables.BusinessRoleTable;
import com.onyxdb.idm.generated.jooq.tables.RoleTable;
import com.onyxdb.idm.models.BusinessRole;
import com.onyxdb.idm.models.Role;

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
public class BusinessRolePostgresRepository implements BusinessRoleRepository {
    private final DSLContext dslContext;
    private final static BusinessRoleTable businessRoleTable = Tables.BUSINESS_ROLE_TABLE;
    private final static BusinessRoleRoleTable businessRoleToRoleTable = Tables.BUSINESS_ROLE_ROLE_TABLE;
    private final static RoleTable roleTable = Tables.ROLE_TABLE;

    @Override
    public Optional<BusinessRole> findById(UUID id) {
        return dslContext.selectFrom(businessRoleTable)
                .where(businessRoleTable.ID.eq(id))
                .fetchOptional(BusinessRole::fromDAO);
    }

    @Override
    public List<BusinessRole> findAll() {
        return dslContext.selectFrom(businessRoleTable)
                .fetch(BusinessRole::fromDAO);
    }


    @Override
    public List<BusinessRole> findByParentId(UUID parentId) {
        return dslContext.selectFrom(businessRoleTable)
                .where(businessRoleTable.PARENT_ID.eq(parentId))
                .fetch(BusinessRole::fromDAO);
    }

    @Override
    public void create(BusinessRole businessRole) {
        dslContext.insertInto(businessRoleTable)
                .set(businessRoleTable.ID, businessRole.id())
                .set(businessRoleTable.NAME, businessRole.name())
                .set(businessRoleTable.DESCRIPTION, businessRole.description())
                .set(businessRoleTable.PARENT_ID, businessRole.parentId())
                .set(businessRoleTable.CREATED_AT, businessRole.createdAt())
                .set(businessRoleTable.UPDATED_AT, businessRole.updatedAt())
                .execute();
    }

    @Override
    public void update(BusinessRole businessRole) {
        dslContext.update(businessRoleTable)
                .set(businessRoleTable.NAME, businessRole.name())
                .set(businessRoleTable.DESCRIPTION, businessRole.description())
                .set(businessRoleTable.PARENT_ID, businessRole.parentId())
                .set(businessRoleTable.UPDATED_AT, businessRole.updatedAt())
                .where(businessRoleTable.ID.eq(businessRole.id()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(businessRoleTable)
                .where(businessRoleTable.ID.eq(id))
                .execute();
    }

    @Override
    public void addRole(UUID businessRoleId, UUID roleId) {
        dslContext.insertInto(businessRoleToRoleTable)
                .set(businessRoleToRoleTable.BUSINESS_ROLE_ID, businessRoleId)
                .set(businessRoleToRoleTable.ROLE_ID, roleId)
                .execute();
    }

    @Override
    public void removeRole(UUID businessRoleId, UUID roleId) {
        dslContext.deleteFrom(businessRoleToRoleTable)
                .where(businessRoleToRoleTable.ROLE_ID.eq(roleId)
                        .and(businessRoleToRoleTable.BUSINESS_ROLE_ID.eq(businessRoleId)))
                .execute();
    }

    @Override
    public List<Role> getRoleByBusinessRoleId(UUID businessRoleId) {
        return dslContext.selectFrom(businessRoleToRoleTable)
                .where(businessRoleToRoleTable.BUSINESS_ROLE_ID.eq(businessRoleId))
                .fetch(link -> dslContext.selectFrom(roleTable)
                        .where(roleTable.ID.eq(link.getRoleId()))
                        .fetchOne(Role::fromDAO)
                );
    }
}
