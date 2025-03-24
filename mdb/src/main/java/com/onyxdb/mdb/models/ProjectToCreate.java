//package com.onyxdb.mdb.models;
//
//import java.util.UUID;
//
//import com.onyxdb.mdb.generated.openapi.models.V1CreateProjectRequest;
//
///**
// * @author foxleren
// */
//public record ProjectToCreate(
//        String name,
//        UUID ownerId
//) {
//    public static ProjectToCreate fromV1CreateProjectRequest(V1CreateProjectRequest rq) {
//        return new ProjectToCreate(
//                rq.getName(),
//                rq.getOwnerId()
//        );
//    }
//}
