package com.onyxdb.idm.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.onyxdb.idm.codegen.types.Organization;
import graphql.schema.DataFetchingEnvironment;

@DgsComponent
public class OrganizationDataFetcher {
  @DgsData(
      parentType = "Query",
      field = "organization"
  )
  public Organization getOrganization(DataFetchingEnvironment dataFetchingEnvironment) {
    return null;
  }
}
