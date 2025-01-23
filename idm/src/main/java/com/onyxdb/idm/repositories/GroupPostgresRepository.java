package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.tables.AccountGroupTable;
import com.onyxdb.idm.generated.jooq.tables.AccountTable;
import com.onyxdb.idm.generated.jooq.tables.GroupTable;
import com.onyxdb.idm.generated.jooq.tables.GroupRoleTable;
import com.onyxdb.idm.generated.jooq.tables.RoleTable;
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
public class GroupPostgresRepository implements GroupRepository {
    private final DSLContext dslContext;
    private final RoleTable roleTable = RoleTable.ROLE_TABLE;
    private final GroupTable groupTable = GroupTable.GROUP_TABLE;
    private final AccountTable accountTable = AccountTable.ACCOUNT_TABLE;
    private final AccountGroupTable accountGroupTable = AccountGroupTable.ACCOUNT_GROUP_TABLE;
    private final GroupRoleTable groupRoleTable = GroupRoleTable.GROUP_ROLE_TABLE;
    private final AccountPostgresRepository accountRepository;
    private final RolePostgresRepository roleRepository;

    @Override
    public Optional<GroupDTO> findById(UUID id) {
        return dslContext.selectFrom(groupTable)
                .where(groupTable.ID.eq(id))
                .fetchOptional()
                .map(record -> {
                    List<AccountDTO> accounts = dslContext.selectFrom(accountTable)
                            .where(accountTable.ID.in(dslContext.select(accountGroupTable.ACCOUNT_ID)
                                    .from(accountGroupTable)
                                    .where(accountGroupTable.GROUP_ID.eq(id))))
                            .fetch()
                            .map(accountRecord -> accountRepository.findById(accountRecord.getId()).orElse(null))
                            .stream()
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toList());

                    List<GroupDTO> nestedGroups = dslContext.selectFrom(groupTable)
                            .where(groupTable.ID.in(dslContext.select(accountGroupTable.GROUP_ID)
                                            .from(accountGroupTable)
                                            .where(accountGroupTable.GROUP_ID.eq(id)
                                                    .and(accountGroupTable.ACCOUNT_ID.isNull()))))
                                    .fetch()
                                    .map(groupRecord -> findById(groupRecord.getId()).orElse(null))
                                    .stream()
                                    .filter(Optional::isPresent)
                                    .map(Optional::get)
                                    .collect(Collectors.toList());

                    List<RoleDTO> roles = dslContext.selectFrom(roleTable)
                            .where(roleTable.ID.in(dslContext.select(groupRoleTable.ROLE_ID)
                                    .from(groupRoleTable)
                                    .where(groupRoleTable.GROUP_ID.eq(id))))
                            .fetch()
                            .map(roleRecord -> roleRepository.findById(roleRecord.getId()).orElse(null))
                            .stream()
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toList());

                    return GroupDTO.builder()
                            .id(record.getId())
                            .name(record.getName())
                            .accounts(accounts)
                            .groups(nestedGroups)
                            .roles(roles)
                            .build();
                });
    }

    @Override
    public List<GroupDTO> findAll() {
        return dslContext.selectFrom(groupTable)
                .fetch()
                .map(record -> GroupDTO.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .build());
    }

    @Override
    public void create(GroupDTO group) {
        dslContext.insertInto(groupTable)
                .set(groupTable.ID, group.getId())
                .set(groupTable.NAME, group.getName())
                .execute();
    }

    @Override
    public void update(GroupDTO group) {
        dslContext.update(groupTable)
                .set(groupTable.NAME, group.getName())
                .where(groupTable.ID.eq(group.getId()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(groupTable)
                .where(groupTable.ID.eq(id))
                .execute();
    }
}