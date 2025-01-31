package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.AccountBusinessRoleTable;
import com.onyxdb.idm.generated.jooq.tables.AccountRoleTable;
import com.onyxdb.idm.generated.jooq.tables.AccountTable;
import com.onyxdb.idm.generated.jooq.tables.BusinessRoleTable;
import com.onyxdb.idm.generated.jooq.tables.RoleTable;
import com.onyxdb.idm.models.Account;
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
public class AccountPostgresRepository implements AccountRepository {
    private static DSLContext dslContext;
    private final static AccountTable accountTable = Tables.ACCOUNT_TABLE;
    private final static AccountBusinessRoleTable accountBusinessRoleTable = Tables.ACCOUNT_BUSINESS_ROLE_TABLE;
    private final static BusinessRoleTable businessRoleTable = Tables.BUSINESS_ROLE_TABLE;
    private final static AccountRoleTable accountRoleTable = Tables.ACCOUNT_ROLE_TABLE;
    private final static RoleTable roleTable = Tables.ROLE_TABLE;

    @Override
    public Optional<Account> findById(UUID id) {
        return dslContext.selectFrom(accountTable)
                .where(accountTable.ID.eq(id))
                .fetchOptional(Account::fromDAO);
    }

    @Override
    public List<Account> findAll() {
        return dslContext.selectFrom(accountTable)
                .fetch(Account::fromDAO);
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
    public void create(Account account) {
        dslContext.insertInto(accountTable)
                .set(accountTable.ID, account.id())
                .set(accountTable.LOGIN, account.login())
                .set(accountTable.PASSWORD, account.password())
                .set(accountTable.EMAIL, account.email())
                .set(accountTable.FIRST_NAME, account.firstName())
                .set(accountTable.LAST_NAME, account.lastName())
                .set(accountTable.CREATED_AT, account.createdAt())
                .set(accountTable.UPDATED_AT, account.updatedAt())
                .execute();
    }

    @Override
    public void update(Account account) {
        dslContext.update(accountTable)
                .set(accountTable.LOGIN, account.login())
                .set(accountTable.PASSWORD, account.password())
                .set(accountTable.EMAIL, account.email())
                .set(accountTable.FIRST_NAME, account.firstName())
                .set(accountTable.LAST_NAME, account.lastName())
                .set(accountTable.UPDATED_AT, account.updatedAt())
                .where(accountTable.ID.eq(account.id()))
                .execute();
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
    public List<Role> getRoleByBusinessRoleId(UUID accountId) {
        return dslContext.selectFrom(accountRoleTable)
                .where(accountRoleTable.ACCOUNT_ID.eq(accountId))
                .fetch(link -> dslContext.selectFrom(roleTable)
                        .where(roleTable.ID.eq(link.getRoleId()))
                        .fetchOne(Role::fromDAO)
                );
    }
}
