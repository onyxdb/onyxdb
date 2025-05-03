package com.onyxdb.platform.mdb.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.ManagedMongoDbUsersApi;
import com.onyxdb.platform.generated.openapi.models.ListMongoRolesResponse;
import com.onyxdb.platform.generated.openapi.models.ListMongoUsersResponse;
import com.onyxdb.platform.generated.openapi.models.MongoUser;
import com.onyxdb.platform.generated.openapi.models.MongoUserToCreate;
import com.onyxdb.platform.generated.openapi.models.V1ScheduledOperationResponse;
import com.onyxdb.platform.mdb.clusters.models.CreateUser;
import com.onyxdb.platform.mdb.clusters.models.MongoRole;
import com.onyxdb.platform.mdb.clusters.models.User;
import com.onyxdb.platform.mdb.users.UserMapper;
import com.onyxdb.platform.mdb.users.UserService;

@RestController
@RequiredArgsConstructor
public class ManagedMongoUserController implements ManagedMongoDbUsersApi {
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public ResponseEntity<ListMongoUsersResponse> listUsers(UUID clusterId) {
        List<User> users = userService.listUsers(clusterId);
        var response = new ListMongoUsersResponse(
                users.stream().map(userMapper::map).toList()
        );

        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<MongoUser> getUser(UUID userId) {
        User user = userService.getUser(userId);
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
        CreateUser createUser = userMapper.map(clusterId, mongoUserToCreate);
        UUID operationId = userService.createUser(createUser);

        return ResponseEntity.ok()
                .body(new V1ScheduledOperationResponse(operationId));
    }

    @Override
    public ResponseEntity<V1ScheduledOperationResponse> deleteUser(UUID userId) {
        UUID operationId = userService.deleteUser(userId);

        return ResponseEntity.ok()
                .body(new V1ScheduledOperationResponse(operationId));
    }
}
