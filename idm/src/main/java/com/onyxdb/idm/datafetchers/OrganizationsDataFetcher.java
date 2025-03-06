package com.onyxdb.idm.datafetchers;

import java.util.List;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.schema.DataFetchingEnvironment;

import com.onyxdb.idm.codegen.types.Organization;

@DgsComponent
public class OrganizationsDataFetcher {
  @DgsData(
      parentType = "Query",
      field = "organizations"
  )
  public List<Organization> getOrganizations(DataFetchingEnvironment dataFetchingEnvironment) {
    return null;
  }
}
