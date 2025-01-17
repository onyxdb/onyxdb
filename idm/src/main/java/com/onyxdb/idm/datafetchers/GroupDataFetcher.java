package com.onyxdb.idm.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.onyxdb.idm.codegen.types.Group;
import graphql.schema.DataFetchingEnvironment;

@DgsComponent
public class GroupDataFetcher {
  @DgsData(
      parentType = "Query",
      field = "group"
  )
  public Group getGroup(DataFetchingEnvironment dataFetchingEnvironment) {
    return null;
  }
}
