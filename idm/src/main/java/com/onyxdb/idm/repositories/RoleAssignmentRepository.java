package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.tables.AccountRoleTable;
import com.onyxdb.idm.generated.jooq.tables.GroupRoleTable;
import com.onyxdb.idm.models.RoleDTO;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RoleAssignmentRepository {
    private final DSLContext dslContext;
    private final AccountRoleTable accountRoleTable = AccountRoleTable.ACCOUNT_ROLE_TABLE;
    private final GroupRoleTable groupRoleTable = GroupRoleTable.GROUP_ROLE_TABLE;
    private final RolePostgresRepository roleRepository;

    public List<RoleDTO> getRolesByAccountId(UUID accountId) {
        return dslContext.selectFrom(accountRoleTable)
                .where(accountRoleTable.ACCOUNT_ID.eq(accountId))
                .fetch()
                .map(record -> roleRepository.findById(record.getRoleId()).orElse(null))
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .distinct()
                .collect(Collectors.toList());
    }

    public void assignRoleToAccount(UUID accountId, UUID roleId) {
        if (dslContext.selectFrom(accountRoleTable)
                .where(accountRoleTable.ACCOUNT_ID.eq(accountId).and(accountRoleTable.ROLE_ID.eq(roleId)))
                .fetchOne() == null) {
            dslContext.insertInto(accountRoleTable)
                    .set(accountRoleTable.ACCOUNT_ID, accountId)
                    .set(accountRoleTable.ROLE_ID, roleId)
                    .execute();
        }
    }

    public List<RoleDTO> getRolesByGroupId(UUID groupId) {
        return dslContext.selectFrom(groupRoleTable)
                .where(groupRoleTable.GROUP_ID.eq(groupId))
                .fetch()
                .map(record -> roleRepository.findById(record.getRoleId()).orElse(null))
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .distinct()
                .collect(Collectors.toList());
    }

    public void assignRoleToGroup(UUID groupId, UUID roleId) {
        if (dslContext.selectFrom(groupRoleTable)
                .where(groupRoleTable.GROUP_ID.eq(groupId).and(groupRoleTable.ROLE_ID.eq(roleId)))
                .fetchOne() == null) {
            dslContext.insertInto(groupRoleTable)
                    .set(groupRoleTable.GROUP_ID, groupId)
                    .set(groupRoleTable.ROLE_ID, roleId)
                    .execute();
        }
    }

    public List<RoleDTO> getRolesByResourceId(UUID resourceId) {
        List<RoleDTO> accountRoles = dslContext.selectFrom(accountRoleTable)
                .where(accountRoleTable.RESOURCE_ID.eq(resourceId))
                .fetch()
                .map(record -> roleRepository.findById(record.getRoleId()).orElse(null))
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .distinct()
                .collect(Collectors.toList());

        List<RoleDTO> groupRoles = dslContext.selectFrom(groupRoleTable)
                .where(groupRoleTable.RESOURCE_ID.eq(resourceId))
                .fetch()
                .map(record -> roleRepository.findById(record.getRoleId()).orElse(null))
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .distinct()
                .collect(Collectors.toList());

        return List.of(accountRoles, groupRoles).stream()
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public void assignRoleToResource(UUID resourceId, UUID roleId) {
        if (dslContext.selectFrom(accountRoleTable)
                .where(accountRoleTable.RESOURCE_ID.eq(resourceId).and(accountRoleTable.ROLE_ID.eq(roleId)))
                .fetchOne() == null) {
            if (dslContext.selectFrom(groupRoleTable)
                    .where(groupRoleTable.RESOURCE_ID.eq(resourceId).and(groupRoleTable.ROLE_ID.eq(roleId)))
                    .fetchOne() == null) {
                dslContext.insertInto(accountRoleTable)
                        .set(accountRoleTable.ACCOUNT_ID, UUID.randomUUID()) // Замените на реальный аккаунт, если нужно
                        .set(accountRoleTable.ROLE_ID, roleId)
                        .set(accountRoleTable.RESOURCE_ID, resourceId)
                        .execute();
            }
        }
    }
}