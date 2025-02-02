package com.onyxdb.idm.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.onyxdb.idm.codegen.types.Product;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;

@DgsComponent
public class ProductsDataFetcher {
  @DgsData(
      parentType = "Query",
      field = "products"
  )
  public List<Product> getProducts(DataFetchingEnvironment dataFetchingEnvironment) {
    return null;
  }
}
