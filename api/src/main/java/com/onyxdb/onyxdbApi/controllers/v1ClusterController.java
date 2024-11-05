package com.onyxdb.onyxdbApi.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.onyxdbApi.generated.openapi.apis.V1ClustersApi;
import com.onyxdb.onyxdbApi.generated.openapi.models.V1CreateClusterRequest;
import com.onyxdb.onyxdbApi.generated.openapi.models.V1CreateClusterResponse;
import com.onyxdb.onyxdbApi.models.Cluster;
import com.onyxdb.onyxdbApi.services.ClusterService;

/**
 * @author foxleren
 */
@RestController
@RequiredArgsConstructor
public class v1ClusterController implements V1ClustersApi {
    private final ClusterService clusterService;

    @Override
    public ResponseEntity<V1CreateClusterResponse> v1ClustersCreateCluster(V1CreateClusterRequest request) {
        var cluster = Cluster.fromV1CreateClusterRequest(request);
        clusterService.createCluster(cluster);
        return null;
    }
}
