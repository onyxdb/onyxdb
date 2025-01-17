package com.onyxdb.idm.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.onyxdb.idm.codegen.types.Project;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;

@DgsComponent
public class OrganizationProjectsDataFetcher {
  @DgsData(
      parentType = "Query",
      field = "organizationProjects"
  )
  public List<Project> getOrganizationProjects(DataFetchingEnvironment dataFetchingEnvironment) {
    return null;
  }
}
