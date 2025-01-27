package com.onyxdb.idm.controllers.v1;

import com.onyxdb.idm.generated.openapi.apis.AccountsApi;
import com.onyxdb.idm.generated.openapi.models.AccountDTO;
import com.onyxdb.idm.models.Account;
import com.onyxdb.idm.services.AccountService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

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
        accountService.create(account);
        return new ResponseEntity<>(account.toDTO(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteAccount(UUID accountId) {
        accountService.delete(accountId);
        return null;
    }

    @Override
    public ResponseEntity<AccountDTO> getAccountById(UUID accountId) {
        Account account = accountService.findById(accountId);
        return ResponseEntity.ok(account.toDTO());
    }

    @Override
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        List<Account> accounts = accountService.findAll();
        List<AccountDTO> accountDTOs = accounts.stream().map(Account::toDTO).toList();
        return ResponseEntity.ok(accountDTOs);
    }

    @Override
    public ResponseEntity<AccountDTO> updateAccount(UUID accountId, AccountDTO accountDTO) {
        accountDTO.setId(accountId);
        Account account = Account.fromDTO(accountDTO);
        accountService.update(account);
        return ResponseEntity.ok(account.toDTO());
    }
}