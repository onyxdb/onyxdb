package com.onyxdb.platform.idm.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.stereotype.Repository;

import com.onyxdb.platform.generated.jooq.Tables;
import com.onyxdb.platform.generated.jooq.tables.AccountBusinessRoleTable;
import com.onyxdb.platform.generated.jooq.tables.BusinessRoleRoleTable;
import com.onyxdb.platform.generated.jooq.tables.BusinessRoleTable;
import com.onyxdb.platform.generated.jooq.tables.RoleTable;
import com.onyxdb.platform.generated.jooq.tables.records.BusinessRoleTableRecord;
import com.onyxdb.platform.idm.models.BusinessRole;
import com.onyxdb.platform.idm.models.PaginatedResult;
import com.onyxdb.platform.idm.models.Role;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.trueCondition;

/**
 * @author ArtemFed
 */
@Repository
@RequiredArgsConstructor
public class BusinessRolePostgresRepository implements BusinessRoleRepository {
    private final static BusinessRoleTable businessRoleTable = Tables.BUSINESS_ROLE_TABLE;
    private final static BusinessRoleRoleTable businessRoleToRoleTable = Tables.BUSINESS_ROLE_ROLE_TABLE;
    private final static AccountBusinessRoleTable accountBusinessRoleTable = Tables.ACCOUNT_BUSINESS_ROLE_TABLE;
    private final static RoleTable roleTable = Tables.ROLE_TABLE;
    private final DSLContext dslContext;

    @Override
    public Optional<BusinessRole> findById(UUID id) {
        return dslContext.selectFrom(businessRoleTable)
                .where(businessRoleTable.ID.eq(id))
                .fetchOptional(BusinessRole::fromDAO);
    }

    @Override
    public PaginatedResult<BusinessRole> findAll(String query, Integer limit, Integer offset) {
        limit = (limit != null) ? limit : Integer.MAX_VALUE;
        offset = (offset != null) ? offset : 0;

        Condition condition = query != null ? businessRoleTable.NAME.containsIgnoreCase(query)
                .or(businessRoleTable.DESCRIPTION.containsIgnoreCase(query))
                : trueCondition();

        Result<BusinessRoleTableRecord> records = dslContext.selectFrom(businessRoleTable)
                .where(condition)
                .orderBy(businessRoleTable.CREATED_AT)
                .limit(limit)
                .offset(offset)
                .fetch();

        List<BusinessRole> data = records.map(record -> BusinessRole.fromDAO(record.into(businessRoleTable)));

        int totalCount = dslContext.fetchCount(businessRoleTable, condition);
        return new PaginatedResult<>(
                data,
                totalCount,
                offset + 1,
                Math.min(offset + limit, totalCount)
        );
    }


    @Override
    public List<BusinessRole> findChildren(UUID parentId) {
        return dslContext.selectFrom(businessRoleTable)
                .where(businessRoleTable.PARENT_ID.eq(parentId))
                .fetch(BusinessRole::fromDAO);
    }

    @Override
    public List<BusinessRole> findAllParents(UUID id) {
        var cte = name("recursive_cte").as(
                select(businessRoleTable.fields())
                        .from(businessRoleTable)
                        .where(businessRoleTable.ID.eq(id))
                        .unionAll(
                                select(businessRoleTable.fields())
                                        .from(businessRoleTable)
                                        .join(table(name("recursive_cte")))
                                        .on(field(name("recursive_cte", "parent_id"), org.jooq.impl.SQLDataType.UUID)
                                                .eq(businessRoleTable.ID)))
        );

        return dslContext.withRecursive(cte)
                .selectFrom(cte)
                .fetch()
                .map(record -> BusinessRole.fromDAO(record.into(BusinessRoleTableRecord.class)));
    }

    @Override
    public List<BusinessRole> findBusinessRolesWithHierarchyByAccountId(UUID accountId) {
        var cte = name("recursive_cte").as(
                select(businessRoleTable.fields())
                        .from(businessRoleTable)
                        .innerJoin(accountBusinessRoleTable)
                        .on(businessRoleTable.ID.eq(accountBusinessRoleTable.BUSINESS_ROLE_ID))
                        .where(accountBusinessRoleTable.ACCOUNT_ID.eq(accountId))
                        .unionAll(
                                select(businessRoleTable.fields())
                                        .from(businessRoleTable)
                                        .join(table(name("recursive_cte")))
                                        .on(field(name("recursive_cte", "parent_id"), org.jooq.impl.SQLDataType.UUID)
                                                .eq(businessRoleTable.ID)))
        );

        return dslContext.withRecursive(cte)
                .selectFrom(cte)
                .orderBy(businessRoleTable.CREATED_AT)
                .fetch()
                .map(record -> BusinessRole.fromDAO(record.into(BusinessRoleTableRecord.class)));
    }

    @Override
    public BusinessRole create(BusinessRole businessRole) {
        var record = dslContext.insertInto(businessRoleTable)
                .set(businessRoleTable.ID, UUID.randomUUID())
                .set(businessRoleTable.NAME, businessRole.name())
                .set(businessRoleTable.SHOP_NAME, businessRole.shop_name())
                .set(businessRoleTable.DESCRIPTION, businessRole.description())
                .set(businessRoleTable.PARENT_ID, businessRole.parentId())
                .set(businessRoleTable.DATA, businessRole.getDataAsJsonb())
                .set(businessRoleTable.CREATED_AT, LocalDateTime.now())
                .set(businessRoleTable.UPDATED_AT, LocalDateTime.now())
                .returning()
                .fetchOne();

        assert record != null;
        return BusinessRole.fromDAO(record);
    }

    @Override
    public BusinessRole update(BusinessRole businessRole) {
        var record = dslContext.update(businessRoleTable)
                .set(businessRoleTable.NAME, businessRole.name())
                .set(businessRoleTable.SHOP_NAME, businessRole.shop_name())
                .set(businessRoleTable.DESCRIPTION, businessRole.description())
                .set(businessRoleTable.PARENT_ID, businessRole.parentId())
                .set(businessRoleTable.DATA, businessRole.getDataAsJsonb())
                .set(businessRoleTable.UPDATED_AT, LocalDateTime.now())
                .where(businessRoleTable.ID.eq(businessRole.id()))
                .returning()
                .fetchOne();

        assert record != null;
        return BusinessRole.fromDAO(record);
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(businessRoleToRoleTable)
                .where(businessRoleToRoleTable.BUSINESS_ROLE_ID.eq(id))
                .execute();

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
    public List<Role> getRoles(UUID businessRoleId) {
        return dslContext.selectFrom(businessRoleToRoleTable)
                .where(businessRoleToRoleTable.BUSINESS_ROLE_ID.eq(businessRoleId))
                .fetch(link -> dslContext.selectFrom(roleTable)
                        .where(roleTable.ID.eq(link.getRoleId()))
                        .orderBy(roleTable.CREATED_AT)
                        .fetchOne(Role::fromDAO)
                );
    }
}
