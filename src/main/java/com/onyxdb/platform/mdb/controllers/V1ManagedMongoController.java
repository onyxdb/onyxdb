package com.onyxdb.platform.mdb.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.V1ManagedMongoDbApi;
import com.onyxdb.platform.generated.openapi.models.MongoListHostsResponse;
import com.onyxdb.platform.generated.openapi.models.V1CreateMongoClusterRequest;
import com.onyxdb.platform.generated.openapi.models.V1CreateMongoClusterResponse;
import com.onyxdb.platform.generated.openapi.models.V1DeleteMongoClusterResponse;
import com.onyxdb.platform.generated.openapi.models.V1ListMongoClustersResponse;
import com.onyxdb.platform.generated.openapi.models.V1MongoClusterResponse;
import com.onyxdb.platform.generated.openapi.models.V1MongoUpdateClusterRequest;
import com.onyxdb.platform.generated.openapi.models.V1ScheduledOperationResponse;
import com.onyxdb.platform.mdb.clusters.ClusterMapper;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.databases.DatabaseMapper;
import com.onyxdb.platform.mdb.hosts.HostMapper;
import com.onyxdb.platform.mdb.hosts.HostService;
import com.onyxdb.platform.mdb.models.Cluster;
import com.onyxdb.platform.mdb.models.ClusterToCreate;
import com.onyxdb.platform.mdb.models.UpdateCluster;
import com.onyxdb.platform.mdb.users.UserMapper;

/**
 * @author foxleren
 */
@RestController
public class V1ManagedMongoController implements V1ManagedMongoDbApi {
    private final ClusterMapper clusterMapper;
    private final ClusterService clusterService;
    private final HostMapper hostMapper;
    private final HostService hostService;
    private final DatabaseMapper databaseMapper;
    private final UserMapper userMapper;

    public V1ManagedMongoController(
            ClusterMapper clusterMapper,
            ClusterService clusterService,
            HostMapper hostMapper,
            HostService hostService,
            DatabaseMapper databaseMapper, UserMapper userMapper
    ) {
        this.clusterMapper = clusterMapper;
        this.clusterService = clusterService;
        this.hostMapper = hostMapper;
        this.hostService = hostService;
        this.databaseMapper = databaseMapper;
        this.userMapper = userMapper;
    }

    @Override
    public ResponseEntity<V1ListMongoClustersResponse> listClusters() {
        List<Cluster> clusters = clusterService.listClusters();
        var response = new V1ListMongoClustersResponse(
                clusters.stream().map(clusterMapper::map).toList()
        );

        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<V1MongoClusterResponse> getCluster(UUID clusterId) {
        Cluster cluster = clusterService.getCluster(clusterId);
        var response = clusterMapper.map(cluster);

        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<V1CreateMongoClusterResponse> createCluster(V1CreateMongoClusterRequest r) {
        ClusterToCreate clusterToCreate = clusterMapper.v1CreateMongoClusterRequestToCreateCluster(r);
        // TODO validate with quotas
        UUID clusterId = clusterService.createCluster(clusterToCreate);

        var response = new V1CreateMongoClusterResponse(clusterId);
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<V1DeleteMongoClusterResponse> deleteCluster(UUID clusterId) {
        UUID operationId = clusterService.deleteCluster(clusterId);

        var response = new V1DeleteMongoClusterResponse(operationId);
        return ResponseEntity.ok().body(response);
    }

//    @Override
//    public ResponseEntity<V1ScheduledOperationResponse> scaleHosts(UUID clusterId, V1MongoScaleHostsRequest rq) {
//        UUID operationId = clusterService.scaleHosts(clusterId, rq.getReplicas());
//
//        return ResponseEntity.ok()
//                .body(new V1ScheduledOperationResponse(operationId));
//    }

    @Override
    public ResponseEntity<V1ScheduledOperationResponse> updateCluster(
            UUID clusterId,
            V1MongoUpdateClusterRequest rq
    ) {
        UpdateCluster updateCluster = clusterMapper.v1MongoUpdateClusterRequestToUpdateCluster(
                clusterId,
                rq
        );
        UUID operationId = clusterService.updateCluster(updateCluster);

        return ResponseEntity.ok()
                .body(new V1ScheduledOperationResponse(operationId));
    }

    @Override
    public ResponseEntity<MongoListHostsResponse> listHosts(UUID clusterId) {
        var mappedMongoHosts = hostService.listMongoHosts(clusterId)
                .stream()
                .map(hostMapper::map)
                .toList();

        return ResponseEntity.ok()
                .body(new MongoListHostsResponse(mappedMongoHosts));
    }
}
