package com.onyxdb.idm.controllers.v1;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.idm.generated.openapi.apis.AccountsApi;
import com.onyxdb.idm.generated.openapi.models.AccountBusinessRolesHistoryDTO;
import com.onyxdb.idm.generated.openapi.models.AccountDTO;
import com.onyxdb.idm.generated.openapi.models.AccountRolesHistoryDTO;
import com.onyxdb.idm.generated.openapi.models.BusinessRoleDTO;
import com.onyxdb.idm.generated.openapi.models.OrganizationUnitDTO;
import com.onyxdb.idm.generated.openapi.models.PaginatedAccountResponse;
import com.onyxdb.idm.generated.openapi.models.RoleDTO;
import com.onyxdb.idm.models.Account;
import com.onyxdb.idm.models.BusinessRole;
import com.onyxdb.idm.models.OrganizationUnit;
import com.onyxdb.idm.models.PaginatedResult;
import com.onyxdb.idm.models.Role;
import com.onyxdb.idm.models.clickhouse.AccountBusinessRolesHistory;
import com.onyxdb.idm.models.clickhouse.AccountRolesHistory;
import com.onyxdb.idm.services.AccountService;

/**
 * @author ArtemFed
 */
@RestController
@RequiredArgsConstructor
public class AccountsController implements AccountsApi {
    private final AccountService accountService;

    @Override
    public ResponseEntity<AccountDTO> createAccount(AccountDTO accountDTO) {
        Account account = Account.fromDTO(accountDTO);
        Account newAccount = accountService.create(account);
        return new ResponseEntity<>(newAccount.toDTO(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteAccount(UUID accountId) {
        accountService.delete(accountId);
        return null;
    }

    @Override
    public ResponseEntity<List<BusinessRoleDTO>> getAccountBusinessRoles(UUID accountId) {
        List<BusinessRole> businessRoles = accountService.getBusinessRoles(accountId);
        List<BusinessRoleDTO> businessRolesDTOs = businessRoles.stream().map(BusinessRole::toDTO).toList();
        return ResponseEntity.ok(businessRolesDTOs);
    }

    @Override
    public ResponseEntity<List<AccountBusinessRolesHistoryDTO>> getAccountBusinessRolesHistory(UUID accountId) {
        List<AccountBusinessRolesHistory> data = accountService.getAccountBusinessRolesHistory(accountId);
        List<AccountBusinessRolesHistoryDTO> dataDTOs = data.stream().map(AccountBusinessRolesHistory::toDTO).toList();
        return ResponseEntity.ok(dataDTOs);
    }

    @Override
    public ResponseEntity<List<AccountRolesHistoryDTO>> getAccountRolesHistory(UUID accountId) {
        List<AccountRolesHistory> data = accountService.getAccountRolesHistory(accountId);
        List<AccountRolesHistoryDTO> dataDTOs = data.stream().map(AccountRolesHistory::toDTO).toList();
        return ResponseEntity.ok(dataDTOs);
    }

    @Override
    public ResponseEntity<AccountDTO> getAccountById(UUID accountId) {
        Account account = accountService.findById(accountId);
        return ResponseEntity.ok(account.toDTO());
    }

    @Override
    public ResponseEntity<List<OrganizationUnitDTO>> getAccountOrganizationUnits(UUID accountId) {
        List<OrganizationUnit> data = accountService.getOrganizationUnits(accountId);
        List<OrganizationUnitDTO> dataDTOs = data.stream().map(OrganizationUnit::toDTO).toList();
        return ResponseEntity.ok(dataDTOs);
    }

    @Override
    public ResponseEntity<List<RoleDTO>> getAccountRoles(UUID accountId) {
        List<Role> roles = accountService.getRoles(accountId);
        List<RoleDTO> roleDTOs = roles.stream().map(Role::toDTO).toList();
        return ResponseEntity.ok(roleDTOs);
    }

    @Override
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
    public ResponseEntity<AccountDTO> updateAccount(UUID accountId, AccountDTO accountDTO) {
        accountDTO.setId(accountId);
        Account account = Account.fromDTO(accountDTO);
        Account newAccount = accountService.update(account);
        return ResponseEntity.ok(newAccount.toDTO());
    }

    @Override
    public ResponseEntity<Void> addBusinessRoleToAccount(UUID accountId, UUID businessRoleId) {
        accountService.addBusinessRole(accountId, businessRoleId);
        return null;
    }

    @Override
    public ResponseEntity<Void> addRoleToAccount(UUID accountId, UUID roleId) {
        accountService.addRole(accountId, roleId);
        return null;
    }


    @Override
    public ResponseEntity<Void> removeBusinessRoleFromAccount(UUID accountId, UUID businessRoleId) {
        accountService.removeBusinessRole(accountId, businessRoleId);
        return null;
    }

    @Override
    public ResponseEntity<Void> removeRoleFromAccount(UUID accountId, UUID roleId) {
        accountService.removeRole(accountId, roleId);
        return null;
    }
}