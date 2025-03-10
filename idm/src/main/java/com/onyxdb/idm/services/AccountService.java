package com.onyxdb.idm.services;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.Account;
import com.onyxdb.idm.models.BusinessRole;
import com.onyxdb.idm.models.OrganizationUnit;
import com.onyxdb.idm.models.PaginatedResult;
import com.onyxdb.idm.models.Role;
import com.onyxdb.idm.repositories.AccountRepository;
import com.onyxdb.idm.repositories.BusinessRoleRepository;
import com.onyxdb.idm.repositories.OrganizationUnitRepository;
import com.onyxdb.idm.repositories.ProductRepository;

/**
 * @author ArtemFed
 */
@Service
@RequiredArgsConstructor
public class AccountService {
//    TODO: Добавить смену пароля

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AccountRepository accountRepository;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final BusinessRoleRepository businessRoleRepository;
    private final ProductRepository productRepository;

    public Account findById(UUID id) {
        return accountRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    public PaginatedResult<Account> findAll(String query, Integer limit, Integer offset) {
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

    public List<OrganizationUnit> getOrganizationUnits(UUID accountId) {
        return accountRepository.getAccountOrganizationUnits(accountId);
    }

    public void addRole(UUID accountId, UUID roleId) {
        accountRepository.addRole(accountId, roleId);
    }

    public void removeRole(UUID accountId, UUID roleId) {
        accountRepository.removeRole(accountId, roleId);
    }

    public List<Role> getRoles(UUID accountId) {
        return accountRepository.getAccountRoles(accountId);
    }

//    public List<String> getAccountPermissions(UUID accountId) {
//
//
//        List<Permission> permissions = permissionRepository.findAccountPermissionsToProduct(accountId, productId, permissionType);
//        return permissions.stream()
//                .map(permission -> String.format("%s-product-%s-%s", permission.resourceType(), productId, permission.actionType()))
//                .collect(Collectors.toList());
//    }
//
//    public List<String> getAccountPermissionsToOrgUnit(UUID accountId, UUID orgUnitId, String permissionType) {
//        List<Permission> permissions = permissionRepository.findAccountPermissionsToOrgUnit(accountId, orgUnitId, permissionType);
//        return permissions.stream()
//                .map(permission -> String.format("%s-org-%s-%s", permission.resourceType(), orgUnitId, permission.actionType()))
//                .collect(Collectors.toList());
//    }
}
