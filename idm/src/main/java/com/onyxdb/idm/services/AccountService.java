package com.onyxdb.idm.services;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.Account;
import com.onyxdb.idm.models.BusinessRole;
import com.onyxdb.idm.repositories.AccountRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author ArtemFed
 */
@Service
@RequiredArgsConstructor
public class AccountService {
//    TODO: Добавить смену пароля

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Account findById(UUID id) {
        return accountRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public void create(Account account) {
        Account forCreate = new Account(
                UUID.randomUUID(),
                account.username(),
                account.password(),
                account.email(),
                account.firstName(),
                account.lastName(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        accountRepository.create(forCreate);
    }

    public void update(Account account) {
        Account forUpdate = new Account(
                account.id(),
                account.username(),
                account.password(),
                account.email(),
                account.firstName(),
                account.lastName(),
                account.createdAt(),
                LocalDateTime.now()
        );
        accountRepository.update(forUpdate);
    }

    public void delete(UUID id) {
        accountRepository.delete(id);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void addBusinessRole(UUID accountId, UUID businessRoleId) {
        accountRepository.addBusinessRole(accountId, businessRoleId);
    }

    public void removeBusinessRole(UUID accountId, UUID businessRoleId) {
        accountRepository.removeBusinessRole(accountId, businessRoleId);
    }

    public List<BusinessRole> getBusinessRoles(UUID accountId) {
        return accountRepository.getAccountBusinessRoles(accountId);
    }
}
