package com.onyxdb.platform.idm.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.AccountsApi;
import com.onyxdb.platform.generated.openapi.models.AccountBusinessRolesHistoryDTO;
import com.onyxdb.platform.generated.openapi.models.AccountDTO;
import com.onyxdb.platform.generated.openapi.models.AccountPostDTO;
import com.onyxdb.platform.generated.openapi.models.AccountRolesAllDTO;
import com.onyxdb.platform.generated.openapi.models.AccountRolesHistoryDTO;
import com.onyxdb.platform.generated.openapi.models.BusinessRoleDTO;
import com.onyxdb.platform.generated.openapi.models.OrganizationUnitDTO;
import com.onyxdb.platform.generated.openapi.models.PaginatedAccountResponse;
import com.onyxdb.platform.generated.openapi.models.RoleDTO;
import com.onyxdb.platform.generated.openapi.models.RoleWithPermissionsDTO;
import com.onyxdb.platform.idm.common.PermissionCheck;
import com.onyxdb.platform.idm.common.PermissionCheckUtils;
import com.onyxdb.platform.idm.common.jwt.SecurityContextUtils;
import com.onyxdb.platform.idm.models.Account;
import com.onyxdb.platform.idm.models.AccountRolesAll;
import com.onyxdb.platform.idm.models.BusinessRole;
import com.onyxdb.platform.idm.models.OrganizationUnit;
import com.onyxdb.platform.idm.models.PaginatedResult;
import com.onyxdb.platform.idm.models.Role;
import com.onyxdb.platform.idm.models.RoleWithPermissions;
import com.onyxdb.platform.idm.models.clickhouse.AccountBusinessRolesHistory;
import com.onyxdb.platform.idm.models.clickhouse.AccountRolesHistory;
import com.onyxdb.platform.idm.models.exceptions.ForbiddenException;
import com.onyxdb.platform.idm.services.AccountService;

/**
 * @author ArtemFed
 */
@RestController
@RequiredArgsConstructor
public class AccountsController implements AccountsApi {
    private final AccountService accountService;

    @Override
    @PermissionCheck(entity = "account", action = "create")
    public ResponseEntity<AccountDTO> createAccount(@Valid AccountPostDTO accountDTO) {
        Account account = Account.fromPostDTO(accountDTO);
        Account newAccount = accountService.create(account);
        return new ResponseEntity<>(newAccount.toDTO(), HttpStatus.CREATED);
    }

    @Override
    @PermissionCheck(entity = "account", action = "delete")
    public ResponseEntity<Void> deleteAccount(UUID accountId) {
        accountService.delete(accountId);
        return null;
    }

    @Override
    @PermissionCheck(entity = "account", action = "get", resourceId = "#accountId")
    public ResponseEntity<AccountRolesAllDTO> getAccountAccess(UUID accountId) {
        AccountRolesAll data = accountService.getAccountAllAccess(accountId);
        return ResponseEntity.ok(data.toDTO());
    }

    @Override
    @PermissionCheck(entity = "account", action = "get", resourceId = "#accountId")
    public ResponseEntity<List<BusinessRoleDTO>> getAccountBusinessRoles(UUID accountId) {
        List<BusinessRole> businessRoles = accountService.getBusinessRoles(accountId);
        List<BusinessRoleDTO> businessRolesDTOs = businessRoles.stream().map(BusinessRole::toDTO).toList();
        return ResponseEntity.ok(businessRolesDTOs);
    }

    @Override
    @PermissionCheck(entity = "account", action = "get", resourceId = "#accountId")
    public ResponseEntity<List<AccountBusinessRolesHistoryDTO>> getAccountBusinessRolesHistory(UUID accountId) {
        List<AccountBusinessRolesHistory> data = accountService.getAccountBusinessRolesHistory(accountId);
        List<AccountBusinessRolesHistoryDTO> dataDTOs = data.stream().map(AccountBusinessRolesHistory::toDTO).toList();
        return ResponseEntity.ok(dataDTOs);
    }

    @Override
    @PermissionCheck(entity = "account", action = "get", resourceId = "#accountId")
    public ResponseEntity<List<AccountRolesHistoryDTO>> getAccountRolesHistory(UUID accountId) {
        List<AccountRolesHistory> data = accountService.getAccountRolesHistory(accountId);
        List<AccountRolesHistoryDTO> dataDTOs = data.stream().map(AccountRolesHistory::toDTO).toList();
        return ResponseEntity.ok(dataDTOs);
    }

    @Override
    @PermissionCheck(entity = "account", action = "get", resourceId = "#accountId")
    public ResponseEntity<AccountDTO> getAccountById(UUID accountId) {
        Account account = accountService.findById(accountId);
        return ResponseEntity.ok(account.toDTO());
    }

    @Override
    @PermissionCheck(entity = "account", action = "get", resourceId = "#accountId")
    public ResponseEntity<List<OrganizationUnitDTO>> getAccountOrganizationUnits(UUID accountId) {
        List<OrganizationUnit> data = accountService.getOrganizationUnits(accountId);
        List<OrganizationUnitDTO> dataDTOs = data.stream().map(OrganizationUnit::toDTO).toList();
        return ResponseEntity.ok(dataDTOs);
    }

    @Override
    @PermissionCheck(entity = "account", action = "get", resourceId = "#accountId")
    public ResponseEntity<List<RoleWithPermissionsDTO>> getAccountPermission(UUID accountId) {
        List<RoleWithPermissions> data = accountService.getAllPermissions(accountId);
        List<RoleWithPermissionsDTO> dataDTOs = data.stream().map(RoleWithPermissions::toDTO).toList();
        return ResponseEntity.ok(dataDTOs);
    }

    @Override
    @PermissionCheck(entity = "account", action = "get", resourceId = "#accountId")
    public ResponseEntity<Map<String, Map<String, Object>>> getAccountPermissionBits(UUID accountId) {
        Map<String, Map<String, Object>> data = accountService.getAllPermissionBitsResponse(accountId);
        return ResponseEntity.ok(data);
    }

    @Override
    @PermissionCheck(entity = "account", action = "get", resourceId = "#accountId")
    public ResponseEntity<List<RoleDTO>> getAccountRoles(UUID accountId) {
        List<Role> roles = accountService.getRoles(accountId);
        List<RoleDTO> roleDTOs = roles.stream().map(Role::toDTO).toList();
        return ResponseEntity.ok(roleDTOs);
    }

    @Override
    @PermissionCheck(entity = "account", action = "get")
    public ResponseEntity<PaginatedAccountResponse> getAllAccounts(String search, Integer limit, Integer offset) {
        PaginatedResult<Account> accounts = accountService.findAll(search, limit, offset);
        List<AccountDTO> accountDTOs = accounts.data().stream().map(Account::toDTO).toList();
        return ResponseEntity.ok(new PaginatedAccountResponse()
                .data(accountDTOs)
                .totalCount(accounts.totalCount())
                .startPosition(accounts.startPosition())
                .endPosition(accounts.endPosition())
        );
    }

    @Override
//    @PermissionCheck(entity = "account", action = "update", resourceId = "#accountId")
    public ResponseEntity<AccountDTO> updateAccount(UUID accountId, AccountPostDTO accountDTO) {
        Account curAccount = SecurityContextUtils.getCurrentAccount();
        if (curAccount.id() != accountId) {
            var entity = "account";
            var action = "update";
            List<String> permissionKeys = PermissionCheckUtils.generatePermissionKeys(entity, action, accountId);
            Map<String, Optional<Map<String, Object>>> permissions = SecurityContextUtils.getCurrentPermissions();
            boolean hasAccess = permissionKeys.stream()
                    .anyMatch(permissions::containsKey);
            if (!hasAccess) {
                throw new ForbiddenException(
                        String.format("Access Forbidden. Your token has no access to %s-%s-%s", entity, action, accountId));
            }
        }

        accountDTO.setId(accountId);
        Account account = Account.fromPostDTO(accountDTO);
        Account newAccount = accountService.update(account);
        return ResponseEntity.ok(newAccount.toDTO());
    }

    @Override
    @PermissionCheck(entity = "account", action = "update", resourceId = "#accountId")
    public ResponseEntity<Void> addBusinessRoleToAccount(UUID accountId, UUID businessRoleId) {
        accountService.addBusinessRole(accountId, businessRoleId);
        return null;
    }

    @Override
    @PermissionCheck(entity = "account", action = "update", resourceId = "#accountId")
    public ResponseEntity<Void> addRoleToAccount(UUID accountId, UUID roleId) {
        accountService.addRole(accountId, roleId);
        return null;
    }


    @Override
    @PermissionCheck(entity = "account", action = "update", resourceId = "#accountId")
    public ResponseEntity<Void> removeBusinessRoleFromAccount(UUID accountId, UUID businessRoleId) {
        accountService.removeBusinessRole(accountId, businessRoleId);
        return null;
    }

    @Override
    @PermissionCheck(entity = "account", action = "update", resourceId = "#accountId")
    public ResponseEntity<Void> removeRoleFromAccount(UUID accountId, UUID roleId) {
        accountService.removeRole(accountId, roleId);
        return null;
    }
}