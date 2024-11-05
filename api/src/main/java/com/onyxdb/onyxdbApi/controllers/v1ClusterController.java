package com.onyxdb.onyxdbApi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.onyxdbApi.generated.openapi.apis.V1ClustersApi;
import com.onyxdb.onyxdbApi.generated.openapi.models.CreateClusterRequest;
import com.onyxdb.onyxdbApi.generated.openapi.models.CreateClusterResponse;
import com.onyxdb.onyxdbApi.models.Cluster;
import com.onyxdb.onyxdbApi.models.Mongo6_0Config;
import com.onyxdb.onyxdbApi.services.ClusterService;

/**
 * @author foxleren
 */
@RestController
@RequiredArgsConstructor
public class v1ClusterController implements V1ClustersApi {
    private final ClusterService clusterService;
    private final ObjectMapper objectMapper;

//    @Override
//    public ResponseEntity<CreateClusterResponse> managedMongoDbCreateCluster(CreateClusterRequest request) {
//        var cluster = Cluster.fromCreateClusterRequest(request);
//
//        System.err.println(cluster);
////        clusterService.createCluster(cluster);
////        var response = new CreateClusterResponse(cluster.id());
//
//        return null;
////        return ResponseEntity.ok(response);
//    }

    @Override
    public ResponseEntity<CreateClusterResponse> v1ClustersCreateCluster(CreateClusterRequest request) {
        System.err.println(request);
//        var classAObj = objectMapper.convertValue(request.getStorage().getDiskSize(), Mongo6_0Config.class);
//        System.err.println(classAObj);
//        var cluster = Cluster.fromCreateClusterRequest(request);
//        System.err.println(cluster);
        return null;
    }
}
