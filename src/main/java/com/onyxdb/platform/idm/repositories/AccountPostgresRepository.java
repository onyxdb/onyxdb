package com.onyxdb.platform.idm.repositories;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.onyxdb.platform.generated.jooq.Tables;
import com.onyxdb.platform.generated.jooq.tables.AccountBusinessRoleTable;
import com.onyxdb.platform.generated.jooq.tables.AccountOuTable;
import com.onyxdb.platform.generated.jooq.tables.AccountRoleTable;
import com.onyxdb.platform.generated.jooq.tables.AccountTable;
import com.onyxdb.platform.generated.jooq.tables.BusinessRoleRoleTable;
import com.onyxdb.platform.generated.jooq.tables.BusinessRoleTable;
import com.onyxdb.platform.generated.jooq.tables.OrganizationUnitTable;
import com.onyxdb.platform.generated.jooq.tables.PermissionTable;
import com.onyxdb.platform.generated.jooq.tables.RolePermissionTable;
import com.onyxdb.platform.generated.jooq.tables.RoleTable;
import com.onyxdb.platform.generated.jooq.tables.records.AccountTableRecord;
import com.onyxdb.platform.idm.models.Account;
import com.onyxdb.platform.idm.models.BusinessRole;
import com.onyxdb.platform.idm.models.BusinessRoleWithRoles;
import com.onyxdb.platform.idm.models.OrganizationUnit;
import com.onyxdb.platform.idm.models.PaginatedResult;
import com.onyxdb.platform.idm.models.Permission;
import com.onyxdb.platform.idm.models.Role;
import com.onyxdb.platform.idm.models.RoleWithPermissions;

import static org.jooq.impl.DSL.trueCondition;

/**
 * @author ArtemFed
 */
@Repository
@RequiredArgsConstructor
public class AccountPostgresRepository implements AccountRepository {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final static AccountTable accountTable = Tables.ACCOUNT_TABLE;
    private final static AccountBusinessRoleTable accountBusinessRoleTable = Tables.ACCOUNT_BUSINESS_ROLE_TABLE;
    private final static AccountRoleTable accountRoleTable = Tables.ACCOUNT_ROLE_TABLE;
    private final static BusinessRoleTable businessRoleTable = Tables.BUSINESS_ROLE_TABLE;
    private final static BusinessRoleRoleTable businessRoleRoleTable = Tables.BUSINESS_ROLE_ROLE_TABLE;
    private final static RolePermissionTable rolePermissionTable = Tables.ROLE_PERMISSION_TABLE;
    private final static RoleTable roleTable = Tables.ROLE_TABLE;
    private final static PermissionTable permissionTable = Tables.PERMISSION_TABLE;
    private final static OrganizationUnitTable organizationUnitTable = Tables.ORGANIZATION_UNIT_TABLE;
    private final static AccountOuTable organizationUnitAccountTable = Tables.ACCOUNT_OU_TABLE;

    private final DSLContext dslContext;

    @Override
    public int count() {
        return dslContext.fetchCount(accountTable);
    }

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
                .where(condition.and(accountTable.IS_DELETED.eq(false)))
                .orderBy(accountTable.CREATED_AT)
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
                .set(accountTable.ID, account.id() == null ? UUID.randomUUID() : account.id())
                .set(accountTable.LOGIN, account.login())
                .set(accountTable.PASSWORD, passwordEncoder.encode(account.password()))
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
                .set(accountTable.PASSWORD, passwordEncoder.encode(account.password()))
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
        dslContext.update(accountTable)
                .set(accountTable.DELETED_AT, LocalDateTime.now())
                .set(accountTable.IS_DELETED, true)
                .where(accountTable.ID.eq(id))
                .returning()
                .fetchOne();
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
                        .orderBy(businessRoleTable.CREATED_AT)
                        .fetchOne(BusinessRole::fromDAO));
    }

    @Override
    public List<OrganizationUnit> getAccountOrganizationUnits(UUID accountId) {
        return dslContext.selectFrom(organizationUnitAccountTable)
                .where(organizationUnitAccountTable.ACCOUNT_ID.eq(accountId))
                .fetch(link -> dslContext.selectFrom(organizationUnitTable)
                        .where(organizationUnitTable.ID.eq(link.getOuId()))
                        .orderBy(organizationUnitTable.CREATED_AT)
                        .fetchOne(OrganizationUnit::fromDAO));
    }

    @Override
    public List<Role> getAccountRoles(UUID accountId) {
        return dslContext.selectFrom(accountRoleTable)
                .where(accountRoleTable.ACCOUNT_ID.eq(accountId))
                .fetch(link -> dslContext.selectFrom(roleTable)
                        .where(roleTable.ID.eq(link.getRoleId()))
                        .orderBy(roleTable.CREATED_AT)
                        .fetchOne(Role::fromDAO));
    }

    @Override
    public List<RoleWithPermissions> getDirectRolesWithPermissions(UUID accountId) {
        // Запрос для прямых ролей и их пермиссий
        var result = dslContext.select(roleTable.asterisk(), permissionTable.asterisk())
                .from(accountRoleTable)
                .join(roleTable).on(accountRoleTable.ROLE_ID.eq(roleTable.ID))
                .leftJoin(rolePermissionTable).on(roleTable.ID.eq(rolePermissionTable.ROLE_ID))
                .leftJoin(permissionTable).on(rolePermissionTable.PERMISSION_ID.eq(permissionTable.ID))
                .where(accountRoleTable.ACCOUNT_ID.eq(accountId))
                .fetch();

        // Группируем роли и их пермиссии
        Map<Role, List<Permission>> rolePermissions = new LinkedHashMap<>();

        for (var record : result) {
            Role role = record.into(roleTable).into(Role.class);
            Permission permission = record.into(permissionTable).into(Permission.class);

            if (!rolePermissions.containsKey(role)) {
                rolePermissions.put(role, new ArrayList<>());
            }

            if (permission.id() != null) {
                rolePermissions.get(role).add(permission);
            }
        }

        return rolePermissions.entrySet().stream()
                .map(entry -> new RoleWithPermissions(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BusinessRoleWithRoles> getBusinessRolesWithRoles(UUID accountId) {
        // Запрос для бизнес-ролей и их ролей с пермиссиями
        var result = dslContext.select(
                        businessRoleTable.asterisk(),
                        roleTable.asterisk(),
                        permissionTable.asterisk()
                )
                .from(accountBusinessRoleTable)
                .join(businessRoleTable).on(accountBusinessRoleTable.BUSINESS_ROLE_ID.eq(businessRoleTable.ID))
                .join(businessRoleRoleTable).on(businessRoleTable.ID.eq(businessRoleRoleTable.BUSINESS_ROLE_ID))
                .join(roleTable).on(businessRoleRoleTable.ROLE_ID.eq(roleTable.ID))
                .leftJoin(rolePermissionTable).on(roleTable.ID.eq(rolePermissionTable.ROLE_ID))
                .leftJoin(permissionTable).on(rolePermissionTable.PERMISSION_ID.eq(permissionTable.ID))
                .where(accountBusinessRoleTable.ACCOUNT_ID.eq(accountId))
                .fetch();

        // Группируем по бизнес-ролям, затем по ролям
        Map<BusinessRole, Map<Role, List<Permission>>> grouped = new LinkedHashMap<>();

        for (var record : result) {
            BusinessRole businessRole = record.into(businessRoleRoleTable).into(BusinessRole.class);
            Role role = record.into(roleTable).into(Role.class);
            Permission permission = record.into(permissionTable).into(Permission.class);

            grouped.computeIfAbsent(businessRole, k -> new LinkedHashMap<>())
                    .computeIfAbsent(role, k -> new ArrayList<>())
                    .add(permission);
        }

        // Преобразуем в итоговую структуру
        return grouped.entrySet().stream()
                .map(entry -> {
                    List<RoleWithPermissions> rolePermissions = entry.getValue().entrySet().stream()
                            .map(e -> new RoleWithPermissions(
                                    e.getKey(),
                                    e.getValue().stream().filter(p -> p.id() != null).collect(Collectors.toList())
                            )).collect(Collectors.toList());

                    return new BusinessRoleWithRoles(entry.getKey(), rolePermissions);
                })
                .collect(Collectors.toList());
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
    public List<Permission> getPermissions(UUID accountId) {
        return dslContext.selectFrom(accountRoleTable)
                .where(accountRoleTable.ACCOUNT_ID.eq(accountId))
                .fetch(link -> dslContext.selectFrom(rolePermissionTable)
                        .where(rolePermissionTable.ROLE_ID.eq(link.getRoleId()))
                        .fetch(link2 -> dslContext.selectFrom(permissionTable)
                                .where(permissionTable.ID.eq(link2.getPermissionId()))
                                .orderBy(permissionTable.CREATED_AT)
                                .fetchOne(Permission::fromDAO))
                )
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
