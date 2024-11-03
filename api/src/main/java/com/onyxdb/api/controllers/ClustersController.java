package com.onyxdb.api.controllers;

import com.onyxdb.generated.api.apis.OnyxApiV1Api;
import com.onyxdb.generated.api.models.CreateClusterRequest;
import com.onyxdb.generated.api.models.CreateClusterResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author foxleren
 */
@RestController
public class ClustersController implements OnyxApiV1Api {
    @Override
    public ResponseEntity<CreateClusterResponse> apiV1CreateCluster(CreateClusterRequest createClusterRequest) {
        return null;
    }
}
