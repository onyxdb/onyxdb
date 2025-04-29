package com.onyxdb.platform.idm.datafetchers;

import java.util.List;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;

import com.onyxdb.platform.idm.models.Account;
import com.onyxdb.platform.idm.services.AccountService;

@DgsComponent
@RequiredArgsConstructor
public class AccountDataFetcher {
    private final AccountService accountService;

    @DgsData(parentType = "Query", field = "account")
    public com.onyxdb.idm.codegen.types.Account getAccount(DataFetchingEnvironment dfe) {
        return null;
//        UUID id = UUID.fromString(Objects.requireNonNull(dfe.getArgument("id")));
//        return accountService.findById(id)
//                .map(this::toGraphQLAccount)
//                .orElse(null);
    }

    @DgsData(parentType = "Query", field = "accounts")
    public List<com.onyxdb.idm.codegen.types.Account> getAccounts(DataFetchingEnvironment dfe) {
        return null;
//        return accountService.findAll().stream()
//                .map(this::toGraphQLAccount)
//                .collect(Collectors.toList());
    }

    @DgsData(parentType = "Mutation", field = "createAccount")
    public com.onyxdb.idm.codegen.types.Account createAccount(DataFetchingEnvironment dfe) {
        return null;
//        Account account = Account.builder()
//                .id(UUID.randomUUID())
//                .username(dfe.getArgument("username"))
//                .email(dfe.getArgument("email"))
//                .build();
//        accountService.create(account);
//        return toGraphQLAccount(account);
    }

    @DgsData(parentType = "Mutation", field = "updateAccount")
    public com.onyxdb.idm.codegen.types.Account updateAccount(DataFetchingEnvironment dfe) {
        return null;
//        UUID id = UUID.fromString(dfe.getArgument("id"));
//        AccountInput input = dfe.getArgument("input");
//        Account account = Account.builder()
//                .id(id)
//                .username(input.getUsername())
//                .email(input.getEmail())
//                .build();
//        accountService.update(account);
//        return toGraphQLAccount(account);
    }

    @DgsData(parentType = "Mutation", field = "deleteAccount")
    public boolean deleteAccount(DataFetchingEnvironment dfe) {
        return false;
//        UUID id = UUID.fromString(dfe.getArgument("id"));
//        accountService.delete(id);
//        return true;
    }

    private com.onyxdb.idm.codegen.types.Account toGraphQLAccount(Account account) {
        return null;
//        return com.onyxdb.idm.codegen.types.Account.newBuilder()
//                .id(account.getId().toString())
//                .username(account.getUsername())
//                .email(account.getEmail())
//                .build();
    }
}