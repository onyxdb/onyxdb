package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.AccountResourceRoleTable;
import com.onyxdb.idm.models.AccountResourceRole;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AccountResourceRolePostgresRepository implements AccountResourceRoleRepository {

    private final DSLContext dslContext;
    private final AccountResourceRoleTable accountResourceRoleTable = Tables.ACCOUNT_RESOURCE_ROLE_TABLE;

    @Override
    public List<AccountResourceRole> findByAccountId(UUID accountId) {
        return dslContext.selectFrom(accountResourceRoleTable)
                .where(accountResourceRoleTable.ACCOUNT_ID.eq(accountId))
                .fetch(record -> new AccountResourceRole(
                        record.getAccountId(),
                        record.getResourceId(),
                        record.getRoleId()
                ));
    }

    @Override
    public List<AccountResourceRole> findByResourceId(UUID resourceId) {
        return dslContext.selectFrom(accountResourceRoleTable)
                .where(accountResourceRoleTable.RESOURCE_ID.eq(resourceId))
                .fetch(record -> new AccountResourceRole(
                        record.getAccountId(),
                        record.getResourceId(),
                        record.getRoleId()
                ));
    }

    @Override
    public void create(AccountResourceRole accountResourceRole) {
        dslContext.insertInto(accountResourceRoleTable)
                .set(accountResourceRoleTable.ACCOUNT_ID, accountResourceRole.accountId())
                .set(accountResourceRoleTable.RESOURCE_ID, accountResourceRole.resourceId())
                .set(accountResourceRoleTable.ROLE_ID, accountResourceRole.roleId())
                .execute();
    }

    @Override
    public void delete(UUID accountId, UUID resourceId, UUID roleId) {
        dslContext.deleteFrom(accountResourceRoleTable)
                .where(accountResourceRoleTable.ACCOUNT_ID.eq(accountId))
                .and(accountResourceRoleTable.RESOURCE_ID.eq(resourceId))
                .and(accountResourceRoleTable.ROLE_ID.eq(roleId))
                .execute();
    }
}
