package com.onyxdb.idm.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.onyxdb.idm.codegen.types.Project;
import graphql.schema.DataFetchingEnvironment;

@DgsComponent
public class ProjectDataFetcher {
  @DgsData(
      parentType = "Query",
      field = "project"
  )
  public Project getProject(DataFetchingEnvironment dataFetchingEnvironment) {
    return null;
  }
}
