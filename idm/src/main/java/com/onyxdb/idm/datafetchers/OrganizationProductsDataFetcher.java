package com.onyxdb.idm.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.onyxdb.idm.codegen.types.Product;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;

@DgsComponent
public class OrganizationProductsDataFetcher {
  @DgsData(
      parentType = "Query",
      field = "organizationProducts"
  )
  public List<Product> getOrganizationProducts(DataFetchingEnvironment dataFetchingEnvironment) {
    return null;
  }
}
