package com.onyxdb.platform.mdb.users;

import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.JSONB;

import com.onyxdb.platform.generated.jooq.tables.records.PermissionsRecord;
import com.onyxdb.platform.generated.openapi.models.MongoUser;
import com.onyxdb.platform.generated.openapi.models.MongoUserToCreate;
import com.onyxdb.platform.mdb.clusters.models.CreateMongoPermission;
import com.onyxdb.platform.mdb.clusters.models.CreateUser;
import com.onyxdb.platform.mdb.clusters.models.CreateUserWithSecret;
import com.onyxdb.platform.mdb.clusters.models.MongoPermission;
import com.onyxdb.platform.mdb.clusters.models.PermissionData;
import com.onyxdb.platform.mdb.clusters.models.User;
import com.onyxdb.platform.mdb.utils.OnyxdbConsts;
import com.onyxdb.platform.mdb.utils.TimeUtils;

public class UserMapper {
    private final ObjectMapper objectMapper;

    public UserMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public MongoUser map(User u) {
        return new MongoUser(
                u.id(),
                u.name(),
                u.clusterId(),
                u.createdAt(),
                u.createdBy(),
                u.isDeleted(),
                u.deletedAt(),
                u.deletedBy(),
                u.permissions().stream().map(this::map).toList()
        );
    }

    public com.onyxdb.platform.generated.openapi.models.MongoPermission map(MongoPermission p) {
        throw new RuntimeException("FIX ME");
//        return new com.onyxdb.platform.generated.openapi.models.MongoPermission(
//                p.id(),
//                p.databaseId(),
//                p.createdAt(),
//                p.createdBy(),
//                p.isDeleted(),
//                p.deletedAt(),
//                p.deletedBy(),
//                p.data().roles().stream().map(MongoRole::value).toList()
//        );
    }

    public CreateUser map(UUID clusterId, MongoUserToCreate u) {
        return new CreateUser(
                u.getName(),
                u.getPassword(),
                clusterId,
                u.getPermissions().stream().map(this::map).toList()
        );
    }

    public CreateMongoPermission map(com.onyxdb.platform.generated.openapi.models.MongoPermissionToCreate p) {
        throw new RuntimeException("FIX ME");
//        return new CreateMongoPermission(
//                p.getDatabaseId(),
//                p.getRoles().stream().map(MongoRole.R::fromValue).toList()
//        );
    }

    public User createUserWithSecretToUser(CreateUserWithSecret u) {
        UUID userId = UUID.randomUUID();
        return new User(
                userId,
                u.userName(),
                u.passwordSecretName(),
                u.clusterId(),
                TimeUtils.now(),
                OnyxdbConsts.USER_ID,
                false,
                null,
                null,
                u.permissions().stream().map(p -> map(userId, p)).toList()
        );
    }

    public User userToCreateToUser(CreateUser u, String passwordSecretName) {
        UUID userId = UUID.randomUUID();
        return new User(
                userId,
                u.userName(),
                passwordSecretName,
                u.clusterId(),
                TimeUtils.now(),
                OnyxdbConsts.USER_ID,
                false,
                null,
                null,
                u.permissions().stream().map(p -> map(userId, p)).toList()
        );
    }

    public MongoPermission map(UUID userId, CreateMongoPermission p) {
        return new MongoPermission(
                UUID.randomUUID(),
                p.userName(),
                p.databaseName(),
                p.clusterId(),
                TimeUtils.now(),
                OnyxdbConsts.USER_ID,
                false,
                null,
                null,
                new PermissionData(
                        p.roles()
                )
        );
    }

    public PermissionsRecord mapToPermissionsRecord(MongoPermission p) {
        try {
            return new PermissionsRecord(
                    p.id(),
                    p.userName(),
                    p.databaseName(),
                    p.clusterId(),
                    TimeUtils.now(),
                    OnyxdbConsts.USER_ID,
                    false,
                    null,
                    null,
                    JSONB.jsonb(objectMapper.writeValueAsString(p.data()))
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
