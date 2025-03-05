package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.AccountBusinessRoleTable;
import com.onyxdb.idm.generated.jooq.tables.AccountRoleTable;
import com.onyxdb.idm.generated.jooq.tables.AccountTable;
import com.onyxdb.idm.generated.jooq.tables.BusinessRoleTable;
import com.onyxdb.idm.generated.jooq.tables.PermissionTable;
import com.onyxdb.idm.generated.jooq.tables.RolePermissionTable;
import com.onyxdb.idm.generated.jooq.tables.RoleTable;
import com.onyxdb.idm.generated.jooq.tables.records.AccountTableRecord;
import com.onyxdb.idm.models.Account;
import com.onyxdb.idm.models.BusinessRole;
import com.onyxdb.idm.models.PaginatedResult;
import com.onyxdb.idm.models.Permission;
import com.onyxdb.idm.models.Role;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.jooq.Condition;
import org.jooq.Result;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.trueCondition;

/**
 * @author ArtemFed
 */
@Repository
@RequiredArgsConstructor
public class AccountPostgresRepository implements AccountRepository {
    private final DSLContext dslContext;
    private final static AccountTable accountTable = Tables.ACCOUNT_TABLE;
    private final static AccountBusinessRoleTable accountBusinessRoleTable = Tables.ACCOUNT_BUSINESS_ROLE_TABLE;
    private final static BusinessRoleTable businessRoleTable = Tables.BUSINESS_ROLE_TABLE;
    private final static AccountRoleTable accountRoleTable = Tables.ACCOUNT_ROLE_TABLE;
    private final static RolePermissionTable rolePermissionTable = Tables.ROLE_PERMISSION_TABLE;
    private final static PermissionTable permissionTable = Tables.PERMISSION_TABLE;
    private final static RoleTable roleTable = Tables.ROLE_TABLE;

    @Override
    public Optional<Account> findById(UUID id) {
        return dslContext.selectFrom(accountTable)
                .where(accountTable.ID.eq(id))
                .fetchOptional(Account::fromDAO);
    }

    @Override
    public Optional<Account> findByLogin(String login) {
        return dslContext.selectFrom(accountTable)
                .where(accountTable.LOGIN.eq(login))
                .fetchOptional(Account::fromDAO);
    }

    @Override
    public PaginatedResult<Account> findAll(String query, Integer limit, Integer offset) {
        limit = (limit != null) ? limit : Integer.MAX_VALUE;
        offset = (offset != null) ? offset : 0;

        Condition condition = query != null ? accountTable.LOGIN.containsIgnoreCase(query)
                .or(accountTable.EMAIL.containsIgnoreCase(query))
                .or(accountTable.FIRST_NAME.containsIgnoreCase(query))
                .or(accountTable.LAST_NAME.containsIgnoreCase(query))
                : trueCondition();

        Result<AccountTableRecord> records = dslContext.selectFrom(accountTable)
                .where(condition)
                .limit(limit)
                .offset(offset)
                .fetch();

        List<Account> data = records.map(record -> Account.fromDAO(record.into(accountTable)));

        int totalCount = dslContext.fetchCount(accountTable, condition);
        return new PaginatedResult<>(
                data,
                totalCount,
                offset + 1,
                Math.min(offset + limit, totalCount)
        );
    }

//    @Override
//    public void create(Account account) {
//        dslContext.insertInto(
//                        accountTable,
//                        accountTable.ID,
//                        accountTable.USERNAME,
//                        accountTable.PASSWORD,
//                        accountTable.EMAIL,
//                        accountTable.FIRST_NAME,
//                        accountTable.LAST_NAME,
//                        accountTable.CREATED_AT,
//                        accountTable.UPDATED_AT
//                ).valuesOfRecords(account)
//                .execute();
//    }

    @Override
    public Account create(Account account) {
        var record = dslContext.insertInto(accountTable)
                .set(accountTable.ID, UUID.randomUUID())
                .set(accountTable.LOGIN, account.login())
                .set(accountTable.PASSWORD, account.password())
                .set(accountTable.EMAIL, account.email())
                .set(accountTable.FIRST_NAME, account.firstName())
                .set(accountTable.LAST_NAME, account.lastName())
                .set(accountTable.DATA, account.getDataAsJsonb())
                .set(accountTable.CREATED_AT, LocalDateTime.now())
                .set(accountTable.UPDATED_AT, LocalDateTime.now())
                .returning()
                .fetchOne();

        assert record != null;
        return Account.fromDAO(record);
    }

    @Override
    public Account update(Account account) {
        var record = dslContext.update(accountTable)
                .set(accountTable.LOGIN, account.login())
                .set(accountTable.PASSWORD, account.password())
                .set(accountTable.EMAIL, account.email())
                .set(accountTable.FIRST_NAME, account.firstName())
                .set(accountTable.LAST_NAME, account.lastName())
                .set(accountTable.DATA, account.getDataAsJsonb())
                .set(accountTable.UPDATED_AT, LocalDateTime.now())
                .where(accountTable.ID.eq(account.id()))
                .returning()
                .fetchOne();

        assert record != null;
        return Account.fromDAO(record);
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(accountTable)
                .where(accountTable.ID.eq(id))
                .execute();
    }

    @Override
    public void addBusinessRole(UUID accountId, UUID businessRoleId) {
        dslContext.insertInto(accountBusinessRoleTable)
                .set(accountBusinessRoleTable.ACCOUNT_ID, accountId)
                .set(accountBusinessRoleTable.BUSINESS_ROLE_ID, businessRoleId)
                .execute();
    }

    @Override
    public void removeBusinessRole(UUID accountId, UUID businessRoleId) {
        dslContext.deleteFrom(accountBusinessRoleTable)
                .where(accountBusinessRoleTable.ACCOUNT_ID.eq(accountId)
                        .and(accountBusinessRoleTable.BUSINESS_ROLE_ID.eq(businessRoleId)))
                .execute();
    }

    @Override
    public List<BusinessRole> getAccountBusinessRoles(UUID accountId) {
        return dslContext.selectFrom(accountBusinessRoleTable)
                .where(accountBusinessRoleTable.ACCOUNT_ID.eq(accountId))
                .fetch(link -> dslContext.selectFrom(businessRoleTable)
                        .where(businessRoleTable.ID.eq(link.getBusinessRoleId()))
                        .fetchOne(BusinessRole::fromDAO));
    }

    @Override
    public List<Role> getAccountRoles(UUID accountId) {
        return dslContext.selectFrom(accountRoleTable)
                .where(accountRoleTable.ACCOUNT_ID.eq(accountId))
                .fetch(link -> dslContext.selectFrom(roleTable)
                        .where(roleTable.ID.eq(link.getRoleId()))
                        .fetchOne(Role::fromDAO));
    }

    @Override
    public void addRole(UUID accountId, UUID roleId) {
        dslContext.insertInto(accountRoleTable)
                .set(accountRoleTable.ACCOUNT_ID, accountId)
                .set(accountRoleTable.ROLE_ID, roleId)
                .execute();
    }

    @Override
    public void removeRole(UUID accountId, UUID roleId) {
        dslContext.deleteFrom(accountRoleTable)
                .where(accountRoleTable.ROLE_ID.eq(roleId)
                        .and(accountRoleTable.ACCOUNT_ID.eq(accountId)))
                .execute();
    }

    @Override
    public List<Role> getRoles(UUID accountId) {
        return dslContext.selectFrom(accountRoleTable)
                .where(accountRoleTable.ACCOUNT_ID.eq(accountId))
                .fetch(link -> dslContext.selectFrom(roleTable)
                        .where(roleTable.ID.eq(link.getRoleId()))
                        .fetchOne(Role::fromDAO)
                );
    }

    @Override
    public List<Permission> getPermissions(UUID accountId) {
        return dslContext.selectFrom(accountRoleTable)
                .where(accountRoleTable.ACCOUNT_ID.eq(accountId))
                .fetch(link -> dslContext.selectFrom(rolePermissionTable)
                        .where(rolePermissionTable.ROLE_ID.eq(link.getRoleId()))
                        .fetch(link2 -> dslContext.selectFrom(permissionTable)
                                .where(permissionTable.ID.eq(link2.getPermissionId()))
                                .fetchOne(Permission::fromDAO))
                )
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
