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
                .fetchOptional(record -> new Account(
                        record.getId(),
                        record.getUsername(),
                        record.getPassword(),
                        record.getEmail(),
                        record.getFirstName(),
                        record.getLastName(),
                        record.getCreatedAt(),
                        record.getUpdatedAt()
                ));
    }

    @Override
    public List<Account> findAll() {
        return dslContext.selectFrom(accountTable)
                .fetch(record -> new Account(
                        record.getId(),
                        record.getUsername(),
                        record.getPassword(),
                        record.getEmail(),
                        record.getFirstName(),
                        record.getLastName(),
                        record.getCreatedAt(),
                        record.getUpdatedAt()
                ));
    }

    @Override
    public void create(Account account) {
        dslContext.insertInto(accountTable)
                .set(accountTable.ID, account.id())
                .set(accountTable.USERNAME, account.username())
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
                .set(accountTable.USERNAME, account.username())
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
}
