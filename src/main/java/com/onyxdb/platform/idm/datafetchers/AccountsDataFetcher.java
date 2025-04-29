//package com.onyxdb.idm.datafetchers;
//
//import com.netflix.graphql.dgs.DgsComponent;
//import com.netflix.graphql.dgs.DgsData;
//
//import com.onyxdb.idm.codegen.types.Account;
//import com.onyxdb.idm.codegen.types.Group;
//import com.onyxdb.idm.codegen.types.Role;
//import com.onyxdb.idm.codegen.types.RoleType;
//
//import graphql.schema.DataFetchingEnvironment;
//
//import java.util.List;
//
//@DgsComponent
//public class AccountsDataFetcher {
//    @DgsData(
//            parentType = "Query",
//            field = "accounts"
//    )
//    public List<Account> getAccounts(DataFetchingEnvironment dataFetchingEnvironment) {
//        return List.of(new Account(
//                "1",
//                "1",
//                "1",
//                List.of(new Group(
//                        "2",
//                        "2",
//                        List.of(),
//                        List.of(),
//                        List.of()
//                )),
//                List.of(new Role(
//                        "3",
//                        RoleType.ADMIN,
//                        List.of()
//                ))
//        ));
//    }
//}
