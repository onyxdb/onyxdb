package com.onyxdb.idm.datafetchers;

import java.util.List;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.schema.DataFetchingEnvironment;

import com.onyxdb.idm.codegen.types.Product;

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
