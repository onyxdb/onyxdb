package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.tables.AccountGroupTable;
import com.onyxdb.idm.generated.jooq.tables.AccountRoleTable;
import com.onyxdb.idm.generated.jooq.tables.AccountTable;
import com.onyxdb.idm.models.AccountDTO;
import com.onyxdb.idm.models.GroupDTO;
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
public class AccountPostgresRepository implements AccountRepository {
    private final DSLContext dslContext;
    private final AccountTable accountTable = AccountTable.ACCOUNT_TABLE;
    private final AccountGroupTable accountGroupTable = AccountGroupTable.ACCOUNT_GROUP_TABLE;
    private final AccountRoleTable accountRoleTable = AccountRoleTable.ACCOUNT_ROLE_TABLE;
    private final GroupPostgresRepository groupRepository;
    private final RolePostgresRepository roleRepository;

    @Override
    public Optional<AccountDTO> findById(UUID id) {
        return dslContext.selectFrom(accountTable)
                .where(accountTable.ID.eq(id))
                .fetchOptional()
                .map(record -> {
                    List<GroupDTO> groups = dslContext.selectFrom(accountGroupTable)
                            .where(accountGroupTable.ACCOUNT_ID.eq(id))
                            .fetch()
                            .map(recordGroup -> groupRepository.findById(recordGroup.getGroupId()).orElse(null))
                            .stream().toList();

                    List<RoleDTO> roles = dslContext.selectFrom(accountRoleTable)
                            .where(accountRoleTable.ACCOUNT_ID.eq(id))
                            .fetch()
                            .map(recordRole -> roleRepository.findById(recordRole.getRoleId()).orElse(null))
                            .stream().toList();

                    return AccountDTO.builder()
                            .id(record.getId())
                            .username(record.getUsername())
                            .email(record.getEmail())
                            .groups(groups)
                            .roles(roles)
                            .ldapGroups(List.of(record.getLdapGroups()))
                            .adGroups(List.of(record.getAdGroups()))
                            .build();
                });
    }

    @Override
    public List<AccountDTO> findAll() {
        return dslContext.selectFrom(accountTable)
                .fetch()
                .map(record -> AccountDTO.builder()
                        .id(record.getId())
                        .username(record.getUsername())
                        .email(record.getEmail())
                        .ldapGroups(List.of(record.getLdapGroups()))
                        .adGroups(List.of(record.getAdGroups()))
                        .build());
    }

    @Override
    public void create(AccountDTO account) {
        dslContext.insertInto(accountTable)
                .set(accountTable.ID, account.getId())
                .set(accountTable.USERNAME, account.getUsername())
                .set(accountTable.EMAIL, account.getEmail())
                .set(accountTable.LDAP_GROUPS, account.getLdapGroups().toArray(new String[0]))
                .set(accountTable.AD_GROUPS, account.getAdGroups().toArray(new String[0]))
                .execute();
    }

    @Override
    public void update(AccountDTO account) {
        dslContext.update(accountTable)
                .set(accountTable.USERNAME, account.getUsername())
                .set(accountTable.EMAIL, account.getEmail())
                .set(accountTable.LDAP_GROUPS, account.getLdapGroups().toArray(new String[0]))
                .set(accountTable.AD_GROUPS, account.getAdGroups().toArray(new String[0]))
                .where(accountTable.ID.eq(account.getId()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(accountTable)
                .where(accountTable.ID.eq(id))
                .execute();
    }
}