package com.onyxdb.idm.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.onyxdb.idm.codegen.types.Organization;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;

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
