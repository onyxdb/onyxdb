package com.onyxdb.platform.idm.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.schema.DataFetchingEnvironment;

import com.onyxdb.idm.codegen.types.Organization;

@DgsComponent
public class OrganizationDataFetcher {
    @DgsData(
            parentType = "Query",
            field = "organization"
    )
    public Organization getOrganization(DataFetchingEnvironment dataFetchingEnvironment) {
        return null;
    }
}
