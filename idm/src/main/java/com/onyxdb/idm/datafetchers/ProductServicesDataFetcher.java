package com.onyxdb.idm.datafetchers;

import java.util.List;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.schema.DataFetchingEnvironment;

import com.onyxdb.idm.codegen.types.Service;

@DgsComponent
public class ProductServicesDataFetcher {
  @DgsData(
      parentType = "Query",
      field = "productServices"
  )
  public List<Service> getProductServices(DataFetchingEnvironment dataFetchingEnvironment) {
    return null;
  }
}
