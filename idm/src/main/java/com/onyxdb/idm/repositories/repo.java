//package com.onyxdb.idm.repositories;
//
//import com.onyxdb.idm.generated.jooq.tables.AccountTable;
//import com.onyxdb.idm.models.Account;
//import lombok.RequiredArgsConstructor;
//import org.jooq.DSLContext;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Repository
//@RequiredArgsConstructor
//public class AccountPostgresRepository implements AccountRepository {
//    private final DSLContext dslContext;
//    private final AccountTable accountTable = AccountTable.ACCOUNT_TABLE;
//
//    @Override
//    public Optional<Account> findById(UUID id) {
//        return dslContext.selectFrom(accountTable)
//                .where(accountTable.ID.eq(id))
//                .fetchOptional()
//                .map(record -> Account.builder()
//                        .id(record.getId())
//                        .username(record.getUsername())
//                        .email(record.getEmail())
//                        // Добавьте остальные поля при необходимости
//                        .build());
//    }
//
//    @Override
//    public List<Account> findAll() {
//        return dslContext.selectFrom(accountTable)
//                .fetch()
//                .map(record -> Account.builder()
//                        .id(record.getId())
//                        .username(record.getUsername())
//                        .email(record.getEmail())
//                        // Добавьте остальные поля при необходимости
//                        .build())
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public void create(Account account) {
//        dslContext.insertInto(accountTable)
//                .set(accountTable.ID, account.getId())
//                .set(accountTable.USERNAME, account.getUsername())
//                .set(accountTable.EMAIL, account.getEmail())
//                // Добавьте остальные поля при необходимости
//                .execute();
//    }
//
//    @Override
//    public void update(Account account) {
//        dslContext.update(accountTable)
//                .set(accountTable.USERNAME, account.getUsername())
//                .set(accountTable.EMAIL, account.getEmail())
//                // Добавьте остальные поля при необходимости
//                .where(accountTable.ID.eq(account.getId()))
//                .execute();
//    }
//
//    @Override
//    public void delete(UUID id) {
//        dslContext.deleteFrom(accountTable)
//                .where(accountTable.ID.eq(id))
//                .execute();
//    }
//}
//
//// Аналогично для других репозиториев
//
//package com.onyxdb.idm.repositories;
//
//import com.onyxdb.idm.generated.jooq.tables.GroupTable;
//import com.onyxdb.idm.models.Group;
//import lombok.RequiredArgsConstructor;
//import org.jooq.DSLContext;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Repository
//@RequiredArgsConstructor
//public class GroupPostgresRepository implements GroupRepository {
//    private final DSLContext dslContext;
//    private final GroupTable groupTable = GroupTable.GROUP_TABLE;
//
//    @Override
//    public Optional<Group> findById(UUID id) {
//        return dslContext.selectFrom(groupTable)
//                .where(groupTable.ID.eq(id))
//                .fetchOptional()
//                .map(record -> Group.builder()
//                        .id(record.getId())
//                        .name(record.getName())
//                        // Добавьте остальные поля при необходимости
//                        .build());
//    }
//
//    @Override
//    public List<Group> findAll() {
//        return dslContext.selectFrom(groupTable)
//                .fetch()
//                .map(record -> Group.builder()
//                        .id(record.getId())
//                        .name(record.getName())
//                        // Добавьте остальные поля при необходимости
//                        .build())
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public void create(Group group) {
//        dslContext.insertInto(groupTable)
//                .set(groupTable.ID, group.getId())
//                .set(groupTable.NAME, group.getName())
//                // Добавьте остальные поля при необходимости
//                .execute();
//    }
//
//    @Override
//    public void update(Group group) {
//        dslContext.update(groupTable)
//                .set(groupTable.NAME, group.getName())
//                // Добавьте остальные поля при необходимости
//                .where(groupTable.ID.eq(group.getId()))
//                .execute();
//    }
//
//    @Override
//    public void delete(UUID id) {
//        dslContext.deleteFrom(groupTable)
//                .where(groupTable.ID.eq(id))
//                .execute();
//    }
//}
//
//// Аналогично для RoleRepository, ServiceRepository, ProjectRepository, OrganizationRepository
