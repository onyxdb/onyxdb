package com.onyxdb.idm.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.onyxdb.idm.codegen.types.Service;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;

@DgsComponent
public class ProjectServicesDataFetcher {
  @DgsData(
      parentType = "Query",
      field = "projectServices"
  )
  public List<Service> getProjectServices(DataFetchingEnvironment dataFetchingEnvironment) {
    return null;
  }
}
