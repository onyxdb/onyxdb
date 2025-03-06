package com.onyxdb.idm.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.schema.DataFetchingEnvironment;

import com.onyxdb.idm.codegen.types.Group;

@DgsComponent
public class GroupDataFetcher {
    @DgsData(
            parentType = "Query",
            field = "group"
    )
    public Group getGroup(DataFetchingEnvironment dataFetchingEnvironment) {
        return null;
    }
}
