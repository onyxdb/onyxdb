package com.onyxdb.platform.idm.datafetchers;

import java.util.List;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.schema.DataFetchingEnvironment;

import com.onyxdb.idm.codegen.types.Product;

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
