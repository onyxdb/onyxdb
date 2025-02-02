package com.onyxdb.idm.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.onyxdb.idm.codegen.types.Product;
import graphql.schema.DataFetchingEnvironment;

@DgsComponent
public class ProductDataFetcher {
  @DgsData(
      parentType = "Query",
      field = "product"
  )
  public Product getProduct(DataFetchingEnvironment dataFetchingEnvironment) {
    return null;
  }
}
