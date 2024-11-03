package com.onyxdb.onyxdbApi.controllers;

import com.onyxdb.onyxdbApi.generated.openapi.apis.OnyxApiV1ClustersApi;
import com.onyxdb.onyxdbApi.generated.openapi.models.CreateClusterRequest;
import com.onyxdb.onyxdbApi.generated.openapi.models.CreateClusterResponse;
import com.onyxdb.onyxdbApi.models.Cluster;
import com.onyxdb.onyxdbApi.services.ClusterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author foxleren
 */
@RestController
@RequiredArgsConstructor
public class ClusterController implements OnyxApiV1ClustersApi {
    private final ClusterService clusterService;

    @Override
    public ResponseEntity<CreateClusterResponse> apiV1CreateCluster(CreateClusterRequest request) {
        var cluster = Cluster.fromCreateClusterRequest(request);
        clusterService.createCluster(cluster);
        var response = new CreateClusterResponse(cluster.id());
        return ResponseEntity.ok(response);
    }
}
