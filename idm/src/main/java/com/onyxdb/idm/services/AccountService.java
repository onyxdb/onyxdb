package com.onyxdb.idm.services;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.Account;
import com.onyxdb.idm.models.BusinessRole;
import com.onyxdb.idm.models.PaginatedResult;
import com.onyxdb.idm.models.Role;
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
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Account findById(UUID id) {
        return accountRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    public PaginatedResult<Account> findAll(String query, int limit, int offset) {
        return accountRepository.findAll(query, limit, offset);
    }

    public Account create(Account account) {
        return accountRepository.create(account);
    }

    public Account update(Account account) {
        return accountRepository.update(account);
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

    public List<Role> getRoles(UUID accountId) {
        return accountRepository.getAccountRoles(accountId);
    }
}
