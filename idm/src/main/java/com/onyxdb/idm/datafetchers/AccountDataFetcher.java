package com.onyxdb.idm.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.onyxdb.idm.codegen.types.Account;
import graphql.schema.DataFetchingEnvironment;

@DgsComponent
public class AccountDataFetcher {
  @DgsData(
      parentType = "Query",
      field = "account"
  )
  public Account getAccount(DataFetchingEnvironment dataFetchingEnvironment) {
    return null;
  }
}
