package com.onyxdb.idm.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.onyxdb.idm.codegen.types.Service;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;

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
