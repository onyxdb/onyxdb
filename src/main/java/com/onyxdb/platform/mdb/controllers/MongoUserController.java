package com.onyxdb.platform.mdb.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.ManagedMongoDbUsersApi;
import com.onyxdb.platform.generated.openapi.models.CreateMongoUserRequestDTO;
import com.onyxdb.platform.generated.openapi.models.ListMongoRolesResponseDTO;
import com.onyxdb.platform.generated.openapi.models.ListMongoUsersResponseDTO;
import com.onyxdb.platform.generated.openapi.models.ScheduledOperationDTO;
import com.onyxdb.platform.idm.common.jwt.SecurityContextUtils;
import com.onyxdb.platform.idm.models.Account;
import com.onyxdb.platform.mdb.clusters.models.CreateUser;
import com.onyxdb.platform.mdb.clusters.models.MongoRole;
import com.onyxdb.platform.mdb.clusters.models.User;
import com.onyxdb.platform.mdb.users.UserMapper;
import com.onyxdb.platform.mdb.users.UserService;

@RestController
@RequiredArgsConstructor
public class MongoUserController implements ManagedMongoDbUsersApi {
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public ResponseEntity<ListMongoUsersResponseDTO> listUsers(UUID clusterId) {
        List<User> users = userService.listUsers(clusterId);
        var response = new ListMongoUsersResponseDTO(
                users.stream().map(userMapper::userToMongoUserDTO).toList()
        );

        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<ListMongoRolesResponseDTO> listRoles() {
        return ResponseEntity.ok(new ListMongoRolesResponseDTO(
                Arrays.stream(MongoRole.values()).map(MongoRole::value).toList()
        ));
    }

    @Override
    public ResponseEntity<ScheduledOperationDTO> createUser(UUID clusterId, CreateMongoUserRequestDTO rq) {
        Account account = SecurityContextUtils.getCurrentAccount();

        CreateUser createUser = userMapper.createMongoUserRqToCreateUser(clusterId, rq, account.id());
        UUID operationId = userService.createUser(createUser);

        return ResponseEntity.ok(new ScheduledOperationDTO(operationId));
    }

    @Override
    public ResponseEntity<ScheduledOperationDTO> deleteUser(UUID clusterId, String userName) {
        Account account = SecurityContextUtils.getCurrentAccount();
        UUID operationId = userService.deleteUser(clusterId, userName, account.id());

        return ResponseEntity.ok(new ScheduledOperationDTO(operationId));
    }
}
