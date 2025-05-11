package com.onyxdb.platform.idm.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onyxdb.platform.idm.models.Account;
import com.onyxdb.platform.idm.models.AccountRolesAll;
import com.onyxdb.platform.idm.models.BusinessRole;
import com.onyxdb.platform.idm.models.BusinessRoleWithRoles;
import com.onyxdb.platform.idm.models.OrganizationUnit;
import com.onyxdb.platform.idm.models.PaginatedResult;
import com.onyxdb.platform.idm.models.Permission;
import com.onyxdb.platform.idm.models.Role;
import com.onyxdb.platform.idm.models.RoleWithPermissions;
import com.onyxdb.platform.idm.models.clickhouse.AccountBusinessRolesHistory;
import com.onyxdb.platform.idm.models.clickhouse.AccountRolesHistory;
import com.onyxdb.platform.idm.models.exceptions.ResourceNotFoundException;
import com.onyxdb.platform.idm.models.redis.CalculatedAccessAll;
import com.onyxdb.platform.idm.models.redis.CalculatedAccessBits;
import com.onyxdb.platform.idm.repositories.AccountRepository;
import com.onyxdb.platform.idm.repositories.BusinessRoleRepository;
import com.onyxdb.platform.idm.repositories.ClickHouseRepository;
import com.onyxdb.platform.idm.repositories.RedisAccessAllRepository;
import com.onyxdb.platform.idm.repositories.RedisAccessBitsRepository;

/**
 * @author ArtemFed
 */
@Service
@RequiredArgsConstructor
public class AccountService {
    //    TODO: Добавить крутую смену пароля
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository accountRepository;
    private final BusinessRoleRepository businessRoleRepository;
    private final RoleService roleService;
    private final ClickHouseRepository clickHouseRepository;
    private final RedisAccessAllRepository redisAccessAllRepository;
    private final RedisAccessBitsRepository redisAccessBitsRepository;

    public Account findById(UUID id) {
        return accountRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    public Optional<Account> findByIdOptional(UUID id) {
        return accountRepository.findById(id);
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

    public AccountRolesAll getAccountAllAccess(UUID accountId) {
        Optional<CalculatedAccessAll> cachedResult = redisAccessAllRepository.getByAccount(accountId);
        if (cachedResult.isPresent()) {
            if (cachedResult.get().expireDate().isAfter(LocalDateTime.now())) {
                logger.info("Read cached access for {}", accountId);
                return cachedResult.get().data();
            }
            redisAccessAllRepository.deleteData(accountId);
        }

        List<Role> roles = accountRepository.getAccountRoles(accountId);
        List<RoleWithPermissions> rolesPermissions = new ArrayList<>();
        for (Role role : roles) {
            RoleWithPermissions rolePermission = roleService.getPermissionsByRoleId(role.id());
            rolesPermissions.add(rolePermission);
        }

        List<BusinessRole> brs = accountRepository.getAccountBusinessRoles(accountId);
        Set<BusinessRoleWithRoles> allBrs = new HashSet<>();
        for (BusinessRole br : brs) {
            var brRoles = businessRoleRepository.getRoles(br.id());
            List<RoleWithPermissions> brRolesPermissions = new ArrayList<>();
            for (Role role : brRoles) {
                RoleWithPermissions rolePermission = roleService.getPermissionsByRoleId(role.id());
                brRolesPermissions.add(rolePermission);
            }
            allBrs.add(new BusinessRoleWithRoles(br, brRolesPermissions));

            List<BusinessRole> parentBrsRoles = businessRoleRepository.findAllParents(br.id());
            for (BusinessRole br2 : parentBrsRoles) {
                var br2Roles = businessRoleRepository.getRoles(br2.id());
                List<RoleWithPermissions> br2RolesPermissions = new ArrayList<>();
                for (Role role : br2Roles) {
                    RoleWithPermissions rolePermission = roleService.getPermissionsByRoleId(role.id());
                    br2RolesPermissions.add(rolePermission);
                }
                allBrs.add(new BusinessRoleWithRoles(br2, br2RolesPermissions));
            }
        }

        var result = new AccountRolesAll(rolesPermissions, allBrs.stream().toList());
        redisAccessAllRepository.saveData(new CalculatedAccessAll(accountId, result, LocalDateTime.now().plusMinutes(10)));
        return result;
    }


    public String getPermissionBit(Role role, Permission permission) {
        String prefix = "";
//        if (permission.resourceType() != null) {
//            prefix += permission.resourceType() + "-";
//        }

        if (role.entity() != null) {
            prefix += role.entity() + "-";
        } else {
            prefix += "global-";
        }

        prefix += permission.actionType();

        if (role.productId() != null) {
            prefix += "-" + role.productId();
        } else if (role.orgUnitId() != null) {
            prefix += "-" + role.orgUnitId();
        }
        return prefix.toLowerCase();
    }

    public Map<String, Map<String, Object>> filterPermissionBits(Map<String, Optional<Map<String, Object>>> permissions) {
        Map<String, Map<String, Object>> data = new HashMap<>();
        for (Map.Entry<String, Optional<Map<String, Object>>> entry : permissions.entrySet()) {
            data.put(entry.getKey(), entry.getValue().orElse(null));
        }
        return data;
    }

    public Map<String, Map<String, Object>> getAllPermissionBitsResponse(UUID accountId) {
        Map<String, Optional<Map<String, Object>>> optionalData = getAllPermissionBits(accountId);
        return filterPermissionBits(optionalData);
    }

    public Map<String, Optional<Map<String, Object>>> getAllPermissionBits(UUID accountId) {
        Optional<CalculatedAccessBits> cachedResult = redisAccessBitsRepository.getByAccount(accountId);
        if (cachedResult.isPresent()) {
            if (cachedResult.get().expireDate().isAfter(LocalDateTime.now())) {
                logger.info("Read cached bits for {}", accountId);
                return cachedResult.get().bits();
            }
            redisAccessBitsRepository.deleteData(accountId);
        }

        List<RoleWithPermissions> roles = getAllPermissions(accountId);
        Map<String, Optional<Map<String, Object>>> bits = new HashMap<>();
        for (RoleWithPermissions roleWP : roles) {
            var role = roleWP.role();
            for (Permission permission : roleWP.permissions()) {
                bits.put(getPermissionBit(role, permission), Optional.ofNullable(permission.data() == null || permission.data().isEmpty() ? null : permission.data()));
            }
        }
        redisAccessBitsRepository.saveData(new CalculatedAccessBits(accountId, bits, LocalDateTime.now().plusMinutes(10)));
        return bits;
    }

    public List<RoleWithPermissions> getAllPermissions(UUID accountId) {
        List<Role> roles = accountRepository.getAccountRoles(accountId);
        List<BusinessRole> brs = accountRepository.getAccountBusinessRoles(accountId);
        List<Role> allRoles = new ArrayList<>(roles);
        for (BusinessRole br : brs) {
            allRoles.addAll(businessRoleRepository.getRoles(br.id()));

            List<BusinessRole> parentBrsRoles = businessRoleRepository.findAllParents(br.id());
            for (BusinessRole br2 : parentBrsRoles) {
                allRoles.addAll(businessRoleRepository.getRoles(br2.id()));
            }
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
