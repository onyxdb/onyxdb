package com.onyxdb.platform.idm.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.schema.DataFetchingEnvironment;

@DgsComponent
public class HasAccessDataFetcher {
    @DgsData(
            parentType = "Query",
            field = "hasAccess"
    )
    public boolean getHasAccess(DataFetchingEnvironment dataFetchingEnvironment) {
        return false;
    }
}
