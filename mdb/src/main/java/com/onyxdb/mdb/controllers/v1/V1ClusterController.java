package com.onyxdb.mdb.controllers.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.mdb.generated.openapi.apis.V1ClustersApi;
import com.onyxdb.mdb.generated.openapi.models.V1CreateClusterRequest;
import com.onyxdb.mdb.generated.openapi.models.V1CreateClusterResponse;
import com.onyxdb.mdb.models.Cluster;
import com.onyxdb.mdb.services.ClusterService;

/**
 * @author foxleren
 */
@RestController
@RequiredArgsConstructor
public class V1ClusterController implements V1ClustersApi {
    private final ClusterService clusterService;

    @Override
    public ResponseEntity<V1CreateClusterResponse> v1ClustersCreateCluster(V1CreateClusterRequest request) {
        var cluster = Cluster.fromV1CreateClusterRequest(request);
        clusterService.createCluster(cluster);
        var response = new V1CreateClusterResponse(cluster.id());
        return ResponseEntity.ok(response);
    }
}
