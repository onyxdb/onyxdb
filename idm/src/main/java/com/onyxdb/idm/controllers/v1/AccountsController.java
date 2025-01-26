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

@RestController
@RequiredArgsConstructor
public class AccountsController implements AccountsApi {
    private final AccountService accountService;


    /**
     * POST /api/v1/accounts : Create a new account
     *
     * @param accountDTO (required)
     * @return Created (status code 201)
     * or Bad Request (status code 400)
     */
    @Override
    public ResponseEntity<AccountDTO> createAccount(AccountDTO accountDTO) {
        Account account = Account.fromDTO(accountDTO);
        accountService.create(account);
        return new ResponseEntity<>(account.toDTO(), HttpStatus.CREATED);
    }

    /**
     * DELETE /api/v1/accounts/{accountId} : Delete an account by ID
     *
     * @param accountId (required)
     * @return No Content (status code 204)
     * or Not Found (status code 404)
     * or Bad Request (status code 400)
     */
    @Override
    public ResponseEntity<Void> deleteAccount(UUID accountId) {
        accountService.delete(accountId);
        return null;
    }

    /**
     * GET /api/v1/accounts/{accountId} : Get an account by ID
     *
     * @param accountId (required)
     * @return OK (status code 200)
     * or Not Found (status code 404)
     * or Bad Request (status code 400)
     */
    @Override
    public ResponseEntity<AccountDTO> getAccountById(UUID accountId) {
        Account account = accountService.findById(accountId);
        return ResponseEntity.ok(account.toDTO());
    }

    /**
     * GET /api/v1/accounts : Get all accounts
     *
     * @return OK (status code 200)
     * or Bad Request (status code 400)
     */
    @Override
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        List<Account> accounts = accountService.findAll();
        List<AccountDTO> accountDTOs = accounts.stream().map(Account::toDTO).toList();
        return ResponseEntity.ok(accountDTOs);
    }

    /**
     * PUT /api/v1/accounts/{accountId} : Update an account by ID
     *
     * @param accountId  (required)
     * @param accountDTO (required)
     * @return OK (status code 200)
     * or Not Found (status code 404)
     * or Bad Request (status code 400)
     */
    @Override
    public ResponseEntity<AccountDTO> updateAccount(UUID accountId, AccountDTO accountDTO) {
        accountDTO.setId(accountId);
        Account account = Account.fromDTO(accountDTO);
        accountService.update(account);
        return ResponseEntity.ok(account.toDTO());
    }
}