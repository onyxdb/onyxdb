package com.onyxdb.idm.v1;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.idm.generated.openapi.apis.AccountsApi;
import com.onyxdb.idm.generated.openapi.models.Account;
import com.onyxdb.idm.services.AccountService;

/**
 * @author ao.fedorov
 */
@RestController
@RequiredArgsConstructor
public class AccountsController implements AccountsApi {
    private final AccountService accountService;

    /**
     * POST /api/v1/accounts : Create a new account
     *
     * @param account (required)
     * @return Created (status code 201)
     */
    @Override
    public ResponseEntity<Account> createAccount(Account account) {
        return null;
    }

    /**
     * DELETE /api/v1/accounts/{accountId} : Delete an account by ID
     *
     * @param accountId (required)
     * @return No Content (status code 204)
     */
    @Override
    public ResponseEntity<Void> deleteAccount(UUID accountId) {
        return null;
    }

    /**
     * GET /api/v1/accounts/{accountId} : Get an account by ID
     *
     * @param accountId (required)
     * @return OK (status code 200)
     * or Not Found (status code 404)
     */
    @Override
    public ResponseEntity<Account> getAccountById(UUID accountId) {
        return null;
    }

    /**
     * GET /api/v1/accounts : Get all accounts
     *
     * @return OK (status code 200)
     */
    @Override
    public ResponseEntity<List<Account>> getAllAccounts() {
        return null;
    }

    /**
     * PUT /api/v1/accounts/{accountId} : Update an account by ID
     *
     * @param accountId (required)
     * @param account   (required)
     * @return OK (status code 200)
     * or Not Found (status code 404)
     */
    @Override
    public ResponseEntity<Account> updateAccount(UUID accountId, Account account) {
        return null;
    }
}
