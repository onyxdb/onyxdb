package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.tables.AccountTable;
import com.onyxdb.idm.models.AccountDTO;
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
    private final AccountTable accountTable = AccountTable.ACCOUNT_TABLE;

    @Override
    public Optional<AccountDTO> findById(UUID id) {
        return dslContext.selectFrom(accountTable)
                .where(accountTable.ID.eq(id))
                .fetchOptional()
                .map(record -> AccountDTO.builder()
                        .id(record.getId())
                        .username(record.getUsername())
                        .email(record.getEmail())
                        .build());
    }

    @Override
    public List<AccountDTO> findAll() {
        return dslContext.selectFrom(accountTable)
                .fetch()
                .map(record -> AccountDTO.builder()
                        .id(record.getId())
                        .username(record.getUsername())
                        .email(record.getEmail())
                        .build());
    }

    @Override
    public void create(AccountDTO account) {
        dslContext.insertInto(accountTable)
                .set(accountTable.ID, account.getId())
                .set(accountTable.USERNAME, account.getUsername())
                .set(accountTable.EMAIL, account.getEmail())
                .execute();
    }

    @Override
    public void update(AccountDTO account) {
        dslContext.update(accountTable)
                .set(accountTable.USERNAME, account.getUsername())
                .set(accountTable.EMAIL, account.getEmail())
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