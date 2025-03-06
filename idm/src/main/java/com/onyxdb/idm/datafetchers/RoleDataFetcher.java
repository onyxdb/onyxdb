package com.onyxdb.idm.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.schema.DataFetchingEnvironment;

import com.onyxdb.idm.codegen.types.Role;

@DgsComponent
public class RoleDataFetcher {
    @DgsData(
            parentType = "Query",
            field = "role"
    )
    public Role getRole(DataFetchingEnvironment dataFetchingEnvironment) {
        return null;
    }
}
