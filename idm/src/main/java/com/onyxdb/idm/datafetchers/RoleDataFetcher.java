package com.onyxdb.idm.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.onyxdb.idm.codegen.types.Role;
import graphql.schema.DataFetchingEnvironment;

@DgsComponent
public class RoleDataFetcher {
  @DgsData(
      parentType = "Query",
      field = "role"
  )
  public Role getRole(DataFetchingEnvironment dataFetchingEnvironment) {
    return null;
  }
}
