package com.onyxdb.idm.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.schema.DataFetchingEnvironment;

import com.onyxdb.idm.codegen.types.Service;

@DgsComponent
public class ServiceDataFetcher {
  @DgsData(
      parentType = "Query",
      field = "service"
  )
  public Service getService(DataFetchingEnvironment dataFetchingEnvironment) {
    return null;
  }
}
