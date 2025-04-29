package com.onyxdb.platform.idm.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onyxdb.platform.idm.controllers.ResourceNotFoundException;
import com.onyxdb.platform.idm.models.Account;
import com.onyxdb.platform.idm.models.BusinessRole;
import com.onyxdb.platform.idm.models.OrganizationUnit;
import com.onyxdb.platform.idm.models.PaginatedResult;
import com.onyxdb.platform.idm.models.Permission;
import com.onyxdb.platform.idm.models.Role;
import com.onyxdb.platform.idm.models.RoleWithPermissions;
import com.onyxdb.platform.idm.models.clickhouse.AccountBusinessRolesHistory;
import com.onyxdb.platform.idm.models.clickhouse.AccountRolesHistory;
import com.onyxdb.platform.idm.repositories.AccountRepository;
import com.onyxdb.platform.idm.repositories.BusinessRoleRepository;
import com.onyxdb.platform.idm.repositories.ClickHouseRepository;

/**
 * @author ArtemFed
 */
@Service
@RequiredArgsConstructor
public class AccountService {
//    TODO: Добавить смену пароля

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AccountRepository accountRepository;
    private final BusinessRoleRepository businessRoleRepository;
    private final RoleService roleService;
    private final ClickHouseRepository clickHouseRepository;

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
        clickHouseRepository.addAccountBusinessRoleHistory(AccountBusinessRolesHistory.create(accountId, businessRoleId, "add"));
    }

    public void removeBusinessRole(UUID accountId, UUID businessRoleId) {
        accountRepository.removeBusinessRole(accountId, businessRoleId);
        clickHouseRepository.addAccountBusinessRoleHistory(AccountBusinessRolesHistory.create(accountId, businessRoleId, "remove"));
    }

    public List<AccountBusinessRolesHistory> getAccountBusinessRolesHistory(UUID accountId) {
        return clickHouseRepository.getAllAccountBusinessRoleHistory(accountId);
    }

    public List<BusinessRole> getBusinessRoles(UUID accountId) {
        return accountRepository.getAccountBusinessRoles(accountId);
    }

    public List<OrganizationUnit> getOrganizationUnits(UUID accountId) {
        return accountRepository.getAccountOrganizationUnits(accountId);
    }

    public List<AccountRolesHistory> getAccountRolesHistory(UUID accountId) {
        return clickHouseRepository.getAllAccountRoleHistory(accountId);
    }

    public void addRole(UUID accountId, UUID roleId) {
        accountRepository.addRole(accountId, roleId);
        var record = AccountRolesHistory.create(accountId, roleId, "add");
        clickHouseRepository.addAccountRoleHistory(record);
    }

    public void removeRole(UUID accountId, UUID roleId) {
        accountRepository.removeRole(accountId, roleId);
        clickHouseRepository.addAccountRoleHistory(AccountRolesHistory.create(accountId, roleId, "remove"));
    }

    public List<Role> getRoles(UUID accountId) {
        return accountRepository.getAccountRoles(accountId);
    }

    public String getPermissionBit(Role role, Permission permission) {
        String prefix = "";
        if (permission.resourceType() != null) {
            prefix += permission.resourceType() + "-";
        }

        if (role.productId() != null) {
            prefix += "product-" + role.productId();
        } else if (role.orgUnitId() != null) {
            prefix += "orgunit-" + role.orgUnitId();
        } else {
            prefix += "global";
        }
        return (prefix + "-" + permission.actionType()).toLowerCase();
    }

    public Map<String, Map<String, Object>> getAllPermissionBitsResponse(UUID accountId) {
        Map<String, Optional<Map<String, Object>>> optionalData = getAllPermissionBits(accountId);
        Map<String, Map<String, Object>> data = new HashMap<>();
        for (Map.Entry<String, Optional<Map<String, Object>>> entry : optionalData.entrySet()) {
            data.put(entry.getKey(), entry.getValue().orElse(null));
        }
        return data;
    }

    public Map<String, Optional<Map<String, Object>>> getAllPermissionBits(UUID accountId) {
        List<RoleWithPermissions> roles = getAllPermissions(accountId);
        Map<String, Optional<Map<String, Object>>> bits = new HashMap<>();
        for (RoleWithPermissions roleWP : roles) {
            var role = roleWP.role();
            for (Permission permission : roleWP.permissions()) {
                bits.put(getPermissionBit(role, permission), Optional.ofNullable(permission.data().isEmpty() ? null : permission.data()));
            }
        }
        return bits;
    }

    public List<RoleWithPermissions> getAllPermissions(UUID accountId) {
        List<Role> roles = accountRepository.getAccountRoles(accountId);
        List<BusinessRole> brs = accountRepository.getAccountBusinessRoles(accountId);
        List<Role> allRoles = new ArrayList<>(roles);
        for (BusinessRole br : brs) {
            List<Role> brsRoles = businessRoleRepository.getRoles(br.id());
            allRoles.addAll(brsRoles);
        }
        List<RoleWithPermissions> allPermissions = new ArrayList<>();
        for (Role role : allRoles) {
            RoleWithPermissions rolePermission = roleService.getPermissionsByRoleId(role.id());
            allPermissions.add(rolePermission);
        }
        return allPermissions;
    }

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
