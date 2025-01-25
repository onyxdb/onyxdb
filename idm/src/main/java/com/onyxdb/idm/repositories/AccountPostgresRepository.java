package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.AccountTable;
import com.onyxdb.idm.models.Account;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AccountPostgresRepository implements AccountRepository {
    private final DSLContext dslContext;
    private final AccountTable accountTable = Tables.ACCOUNT_TABLE;

    @Override
    public Optional<Account> findById(UUID id) {
        return dslContext.selectFrom(accountTable)
                .where(accountTable.ID.eq(id))
                .fetchOptional(record -> Account.builder()
                        .id(record.getId())
                        .username(record.getUsername())
                        .password(record.getPassword())
                        .email(record.getEmail())
                        .firstName(record.getFirstName())
                        .lastName(record.getLastName())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .build());
    }

    @Override
    public List<Account> findAll() {
        return dslContext.selectFrom(accountTable)
                .fetch(record -> Account.builder()
                        .id(record.getId())
                        .username(record.getUsername())
                        .password(record.getPassword())
                        .email(record.getEmail())
                        .firstName(record.getFirstName())
                        .lastName(record.getLastName())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .build());
    }

    @Override
    public void create(Account account) {
        dslContext.insertInto(accountTable)
                .set(accountTable.ID, account.getId())
                .set(accountTable.USERNAME, account.getUsername())
                .set(accountTable.PASSWORD, account.getPassword())
                .set(accountTable.EMAIL, account.getEmail())
                .set(accountTable.FIRST_NAME, account.getFirstName())
                .set(accountTable.LAST_NAME, account.getLastName())
                .set(accountTable.CREATED_AT, account.getCreatedAt())
                .set(accountTable.UPDATED_AT, account.getUpdatedAt())
                .execute();
    }

    @Override
    public void update(Account account) {
        dslContext.update(accountTable)
                .set(accountTable.USERNAME, account.getUsername())
                .set(accountTable.PASSWORD, account.getPassword())
                .set(accountTable.EMAIL, account.getEmail())
                .set(accountTable.FIRST_NAME, account.getFirstName())
                .set(accountTable.LAST_NAME, account.getLastName())
                .set(accountTable.UPDATED_AT, account.getUpdatedAt())
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
