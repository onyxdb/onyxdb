package com.onyxdb.platform.mdb.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.ManagedMongoDbClustersApi;
import com.onyxdb.platform.generated.openapi.models.CreateMongoClusterRequestDTO;
import com.onyxdb.platform.generated.openapi.models.CreateMongoClusterResponseDTO;
import com.onyxdb.platform.idm.common.jwt.SecurityContextUtils;
import com.onyxdb.platform.idm.models.Account;
import com.onyxdb.platform.mdb.clusters.ClusterMapper;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.clusters.models.CreateCluster;
import com.onyxdb.platform.mdb.clusters.models.CreateClusterResult;

@RestController
public class MongoClusterController implements ManagedMongoDbClustersApi {
    private final ClusterMapper clusterMapper;
    private final ClusterService clusterService;

    public MongoClusterController(ClusterMapper clusterMapper, ClusterService clusterService) {
        this.clusterMapper = clusterMapper;
        this.clusterService = clusterService;
    }

    @Override
    public ResponseEntity<CreateMongoClusterResponseDTO> createCluster(CreateMongoClusterRequestDTO rq) {
        Account account = SecurityContextUtils.getCurrentAccount();

        CreateCluster createCluster = clusterMapper.createMongoClusterRequestDTOtoCreateCluster(rq, account.id());
        CreateClusterResult result = clusterService.createCluster(createCluster);

        return ResponseEntity.ok(new CreateMongoClusterResponseDTO(
                result.clusterId(),
                result.operationId()
        ));
    }
}
