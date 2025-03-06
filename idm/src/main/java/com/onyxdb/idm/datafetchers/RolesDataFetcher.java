package com.onyxdb.idm.datafetchers;

import java.util.List;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.schema.DataFetchingEnvironment;

import com.onyxdb.idm.codegen.types.Role;

@DgsComponent
public class RolesDataFetcher {
    @DgsData(
            parentType = "Query",
            field = "roles"
    )
    public List<Role> getRoles(DataFetchingEnvironment dataFetchingEnvironment) {
        return null;
    }
}
