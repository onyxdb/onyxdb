package com.onyxdb.idm.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;

import com.onyxdb.idm.codegen.types.AccountInput;
import com.onyxdb.idm.models.Account;
import com.onyxdb.idm.services.AccountService;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@DgsComponent
@RequiredArgsConstructor
public class AccountDataFetcher {
    private final AccountService accountService;

    @DgsData(parentType = "Query", field = "account")
    public com.onyxdb.idm.codegen.types.Account getAccount(DataFetchingEnvironment dfe) {
        UUID id = UUID.fromString(Objects.requireNonNull(dfe.getArgument("id")));
        return accountService.findById(id)
                .map(this::toGraphQLAccount)
                .orElse(null);
    }

    @DgsData(parentType = "Query", field = "accounts")
    public List<com.onyxdb.idm.codegen.types.Account> getAccounts(DataFetchingEnvironment dfe) {
        return accountService.findAll().stream()
                .map(this::toGraphQLAccount)
                .collect(Collectors.toList());
    }

    @DgsData(parentType = "Mutation", field = "createAccount")
    public com.onyxdb.idm.codegen.types.Account createAccount(DataFetchingEnvironment dfe) {
        Account account = Account.builder()
                .id(UUID.randomUUID())
                .username(dfe.getArgument("username"))
                .email(dfe.getArgument("email"))
                .build();
        accountService.create(account);
        return toGraphQLAccount(account);
    }

    @DgsData(parentType = "Mutation", field = "updateAccount")
    public com.onyxdb.idm.codegen.types.Account updateAccount(DataFetchingEnvironment dfe) {
        UUID id = UUID.fromString(dfe.getArgument("id"));
        AccountInput input = dfe.getArgument("input");
        Account account = Account.builder()
                .id(id)
                .username(input.getUsername())
                .email(input.getEmail())
                .build();
        accountService.update(account);
        return toGraphQLAccount(account);
    }

    @DgsData(parentType = "Mutation", field = "deleteAccount")
    public boolean deleteAccount(DataFetchingEnvironment dfe) {
        UUID id = UUID.fromString(dfe.getArgument("id"));
        accountService.delete(id);
        return true;
    }

    private com.onyxdb.idm.codegen.types.Account toGraphQLAccount(Account account) {
        return com.onyxdb.idm.codegen.types.Account.newBuilder()
                .id(account.getId().toString())
                .username(account.getUsername())
                .email(account.getEmail())
                .build();
    }
}