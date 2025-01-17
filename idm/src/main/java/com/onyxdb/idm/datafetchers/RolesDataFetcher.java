package com.onyxdb.idm.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.onyxdb.idm.codegen.types.Role;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;

@DgsComponent
public class RolesDataFetcher {
  @DgsData(
      parentType = "Query",
      field = "roles"
  )
  public List<Role> getRoles(DataFetchingEnvironment dataFetchingEnvironment) {
    return null;
  }
}
