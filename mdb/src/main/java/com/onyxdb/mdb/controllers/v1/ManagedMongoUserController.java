package com.onyxdb.mdb.controllers.v1;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.mdb.core.clusters.ClusterService;
import com.onyxdb.mdb.core.clusters.mappers.UserMapper;
import com.onyxdb.mdb.core.clusters.models.MongoRole;
import com.onyxdb.mdb.core.clusters.models.User;
import com.onyxdb.mdb.core.clusters.models.UserToCreate;
import com.onyxdb.mdb.generated.openapi.apis.ManagedMongoDbUsersApi;
import com.onyxdb.mdb.generated.openapi.models.ListMongoRolesResponse;
import com.onyxdb.mdb.generated.openapi.models.ListMongoUsersResponse;
import com.onyxdb.mdb.generated.openapi.models.MongoUser;
import com.onyxdb.mdb.generated.openapi.models.MongoUserToCreate;
import com.onyxdb.mdb.generated.openapi.models.V1ScheduledOperationResponse;

@RestController
public class ManagedMongoUserController implements ManagedMongoDbUsersApi {
    private final ClusterService clusterService;
    private final UserMapper userMapper;

    public ManagedMongoUserController(ClusterService clusterService, UserMapper userMapper) {
        this.clusterService = clusterService;
        this.userMapper = userMapper;
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
    public ResponseEntity<ListMongoRolesResponse> listRoles() {
        return ResponseEntity.ok(new ListMongoRolesResponse(
                Arrays.stream(MongoRole.values()).map(MongoRole::value).toList()
        ));
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
