package com.onyxdb.platform.idm.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.schema.DataFetchingEnvironment;

import com.onyxdb.idm.codegen.types.Product;

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
