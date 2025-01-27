package com.onyxdb.idm.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.onyxdb.idm.codegen.types.Group;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;

@DgsComponent
public class GroupsDataFetcher {
  @DgsData(
      parentType = "Query",
      field = "groups"
  )
  public List<Group> getGroups(DataFetchingEnvironment dataFetchingEnvironment) {
    return null;
  }
}
