package com.onyxdb.idm.services;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.AccountTable;
import com.onyxdb.idm.models.Account;
import com.onyxdb.idm.repositories.AccountRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountTable accountTable = Tables.ACCOUNT_TABLE;

    public Account findById(UUID id) {
        Optional<Account> account = accountRepository.findById(id);
        account.orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        return account.get();
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public void create(Account account) {
        accountRepository.create(account);
    }

    public void update(Account account) {
        accountRepository.update(account);
    }

    public void delete(UUID id) {
        accountRepository.delete(id);
    }
}
