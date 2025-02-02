package com.onyxdb.idm.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.onyxdb.idm.codegen.types.Service;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;

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
