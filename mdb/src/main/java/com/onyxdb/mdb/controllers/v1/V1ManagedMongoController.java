package com.onyxdb.mdb.controllers.v1;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.mdb.core.clusters.ClusterMapper;
import com.onyxdb.mdb.core.clusters.ClusterService;
import com.onyxdb.mdb.core.clusters.HostService;
import com.onyxdb.mdb.core.clusters.mappers.DatabaseMapper;
import com.onyxdb.mdb.core.clusters.mappers.HostMapper;
import com.onyxdb.mdb.core.clusters.mappers.UserMapper;
import com.onyxdb.mdb.core.clusters.models.Cluster;
import com.onyxdb.mdb.core.clusters.models.CreateCluster;
import com.onyxdb.mdb.core.clusters.models.Database;
import com.onyxdb.mdb.core.clusters.models.DatabaseToCreate;
import com.onyxdb.mdb.core.clusters.models.MongoHost;
import com.onyxdb.mdb.core.clusters.models.UpdateCluster;
import com.onyxdb.mdb.core.clusters.models.User;
import com.onyxdb.mdb.core.clusters.models.UserToCreate;
import com.onyxdb.mdb.generated.openapi.apis.V1ManagedMongoDbApi;
import com.onyxdb.mdb.generated.openapi.models.CreateMongoDatabaseRequest;
import com.onyxdb.mdb.generated.openapi.models.ListMongoDatabasesResponse;
import com.onyxdb.mdb.generated.openapi.models.ListMongoUsersResponse;
import com.onyxdb.mdb.generated.openapi.models.MongoListHostsResponse;
import com.onyxdb.mdb.generated.openapi.models.MongoUser;
import com.onyxdb.mdb.generated.openapi.models.MongoUserToCreate;
import com.onyxdb.mdb.generated.openapi.models.UpdateMongoHostsRequest;
import com.onyxdb.mdb.generated.openapi.models.V1CreateMongoClusterRequest;
import com.onyxdb.mdb.generated.openapi.models.V1CreateMongoClusterResponse;
import com.onyxdb.mdb.generated.openapi.models.V1DeleteMongoClusterResponse;
import com.onyxdb.mdb.generated.openapi.models.V1ListMongoClustersResponse;
import com.onyxdb.mdb.generated.openapi.models.V1MongoClusterResponse;
import com.onyxdb.mdb.generated.openapi.models.V1MongoUpdateClusterRequest;
import com.onyxdb.mdb.generated.openapi.models.V1ScheduledOperationResponse;

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
        CreateCluster createCluster = clusterMapper.v1CreateMongoClusterRequestToCreateCluster(r);

        UUID clusterId = clusterService.createCluster(createCluster);

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

    @Override
    public ResponseEntity<Void> updateHosts(UpdateMongoHostsRequest rq) {
        List<MongoHost> mongoHosts = hostMapper.map(rq);
        hostService.updateMongoHosts(mongoHosts);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ListMongoDatabasesResponse> listDatabases(UUID clusterId) {
        List<Database> databases = clusterService.listDatabases(clusterId);
        var response = new ListMongoDatabasesResponse(
                databases.stream().map(databaseMapper::mapToMongoDatabase).toList()
        );

        return ResponseEntity.ok()
                .body(response);
    }

    @Override
    public ResponseEntity<V1ScheduledOperationResponse> createDatabase(UUID clusterId, CreateMongoDatabaseRequest rq) {
        DatabaseToCreate databaseToCreate = databaseMapper.map(clusterId, rq);
        UUID operationId = clusterService.createDatabase(databaseToCreate);

        return ResponseEntity.ok()
                .body(new V1ScheduledOperationResponse(operationId));
    }

    @Override
    public ResponseEntity<V1ScheduledOperationResponse> deleteDatabase(UUID clusterId, UUID databaseId) {
        UUID operationId = clusterService.deleteDatabase(clusterId, databaseId);

        return ResponseEntity.ok()
                .body(new V1ScheduledOperationResponse(operationId));
    }

    @Override
    public ResponseEntity<ListMongoUsersResponse> listUsers(UUID clusterId) {
        List<User> users = clusterService.listUsers(clusterId);
        var response = new ListMongoUsersResponse(
                users.stream().map(userMapper::map).toList()
        );

        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<MongoUser> getUser(UUID userId) {
        User user = clusterService.getUser(userId);
        return ResponseEntity.ok().body(userMapper.map(user));
    }

    @Override
    public ResponseEntity<V1ScheduledOperationResponse> createUser(
            UUID clusterId,
            MongoUserToCreate mongoUserToCreate
    ) {
        UserToCreate userToCreate = userMapper.map(clusterId, mongoUserToCreate);
        UUID operationId = clusterService.createUser(userToCreate);

        return ResponseEntity.ok()
                .body(new V1ScheduledOperationResponse(operationId));
    }

    @Override
    public ResponseEntity<V1ScheduledOperationResponse> deleteUser(UUID userId) {
        UUID operationId = clusterService.deleteUser(userId);

        return ResponseEntity.ok()
                .body(new V1ScheduledOperationResponse(operationId));
    }
}
