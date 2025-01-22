package com.onyxdb.idm.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;

import com.onyxdb.idm.codegen.types.Account;
import com.onyxdb.idm.codegen.types.AccountInput;
import com.onyxdb.idm.models.AccountDTO;
import com.onyxdb.idm.services.AccountService;

import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@DgsComponent
@RequiredArgsConstructor
public class AccountDataFetcher {
    private final AccountService accountService;

    @DgsData(parentType = "Query", field = "account")
    public Account getAccount(DataFetchingEnvironment dfe) {
        UUID id = dfe.getArgument("id");
        return accountService.getAccountById(id)
                .map(this::toGraphQLAccount)
                .orElse(null);
    }

    @DgsData(parentType = "Query", field = "accounts")
    public List<Account> getAccounts(DataFetchingEnvironment dfe) {
        return accountService.getAllAccounts().stream()
                .map(this::toGraphQLAccount)
                .collect(Collectors.toList());
    }

    @DgsData(parentType = "Mutation", field = "createAccount")
    public Account createAccount(DataFetchingEnvironment dfe) {
        AccountInput input = dfe.getArgument("input");
        AccountDTO account = AccountDTO.builder()
                .id(UUID.randomUUID())
                .username(input.getUsername())
                .email(input.getEmail())
                .build();
        return toGraphQLAccount(accountService.createAccount(account));
    }

    @DgsData(parentType = "Mutation", field = "updateAccount")
    public Account updateAccount(DataFetchingEnvironment dfe) {
        UUID id = dfe.getArgument("id");
        AccountInput input = dfe.getArgument("input");
        AccountDTO account = AccountDTO.builder()
                .id(id)
                .username(input.getUsername())
                .email(input.getEmail())
                .build();
        return toGraphQLAccount(accountService.updateAccount(account));
    }

    @DgsData(parentType = "Mutation", field = "deleteAccount")
    public boolean deleteAccount(DataFetchingEnvironment dfe) {
        UUID id = dfe.getArgument("id");
        accountService.deleteAccount(id);
        return true;
    }

    private Account toGraphQLAccount(AccountDTO account) {
        return Account.newBuilder()
                .id(account.getId().toString())
                .username(account.getUsername())
                .email(account.getEmail())
                .build();
    }
}
