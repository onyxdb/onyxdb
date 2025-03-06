package com.onyxdb.idm.datafetchers;

import java.util.List;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.schema.DataFetchingEnvironment;

import com.onyxdb.idm.codegen.types.Service;

@DgsComponent
public class ServicesDataFetcher {
  @DgsData(
      parentType = "Query",
      field = "services"
  )
  public List<Service> getServices(DataFetchingEnvironment dataFetchingEnvironment) {
    return null;
  }
}
