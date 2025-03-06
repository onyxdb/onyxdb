package com.onyxdb.idm.datafetchers;

import java.util.List;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.schema.DataFetchingEnvironment;

import com.onyxdb.idm.codegen.types.Group;

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
