package com.onyxdb.mdb.controllers.v1;

import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.mdb.exceptions.ClusterNotFoundException;
import com.onyxdb.mdb.generated.openapi.apis.V1ClustersApi;
import com.onyxdb.mdb.generated.openapi.models.V1CreateClusterRequest;
import com.onyxdb.mdb.generated.openapi.models.V1CreateClusterResponse;
import com.onyxdb.mdb.generated.openapi.models.V1GetClusterResponse;
import com.onyxdb.mdb.generated.openapi.models.V1UpdateClusterProjectRequest;
import com.onyxdb.mdb.models.Cluster;
import com.onyxdb.mdb.models.ClusterToCreate;
import com.onyxdb.mdb.services.BaseClusterService;

/**
 * @author foxleren
 */
@RestController
@RequiredArgsConstructor
public class V1ClusterController implements V1ClustersApi {
    private final BaseClusterService clusterService;

    @Override
    public ResponseEntity<V1CreateClusterResponse> v1ClustersCreateCluster(V1CreateClusterRequest rq) {
        UUID clusterId = clusterService.create(UUID.randomUUID(), ClusterToCreate.fromV1CreateClusterRequest(rq));
        var response = new V1CreateClusterResponse(clusterId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<V1GetClusterResponse> v1ClustersGetCluster(UUID id) {
        Optional<Cluster> clusterO = clusterService.getByIdO(id);
        var cluster = clusterO.orElseThrow(() -> new ClusterNotFoundException(id));
        var response = cluster.toV1GetClusterResponse();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> v1ClustersUpdateProject(V1UpdateClusterProjectRequest rq) {
        clusterService.updateProject(rq.getClusterId(), rq.getProjectId());
        return ResponseEntity.ok(null);
    }
}
