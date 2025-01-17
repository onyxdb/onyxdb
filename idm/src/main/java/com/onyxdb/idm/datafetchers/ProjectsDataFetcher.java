package com.onyxdb.idm.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.onyxdb.idm.codegen.types.Project;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;

@DgsComponent
public class ProjectsDataFetcher {
  @DgsData(
      parentType = "Query",
      field = "projects"
  )
  public List<Project> getProjects(DataFetchingEnvironment dataFetchingEnvironment) {
    return null;
  }
}
