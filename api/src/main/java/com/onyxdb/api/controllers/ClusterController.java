package com.onyxdb.api.controllers;

import com.onyxdb.generated.api.apis.OnyxApiV1ClustersApi;
import com.onyxdb.generated.api.models.CreateClusterRequest;
import com.onyxdb.generated.api.models.CreateClusterResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author foxleren
 */
@RestController
public class ClusterController implements OnyxApiV1ClustersApi {
    @Override
    public ResponseEntity<CreateClusterResponse> apiV1CreateCluster(CreateClusterRequest createClusterRequest) {
        return null;
    }
}
