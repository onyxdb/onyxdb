package com.onyxdb.idm.services;

import com.onyxdb.idm.models.AccountDTO;
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

    public Optional<AccountDTO> getAccountById(UUID id) {
        return accountRepository.findById(id);
    }

    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll();
    }

    public AccountDTO createAccount(AccountDTO account) {
        accountRepository.create(account);
        return account;
    }

    public AccountDTO updateAccount(AccountDTO account) {
        accountRepository.update(account);
        return account;
    }

    public void deleteAccount(UUID id) {
        accountRepository.delete(id);
    }
}
