package com.onyxdb.platform.mdb.users;

import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.JSONB;

import com.onyxdb.platform.core.clusters.models.MongoPermission;
import com.onyxdb.platform.core.clusters.models.MongoPermissionToCreate;
import com.onyxdb.platform.core.clusters.models.MongoRole;
import com.onyxdb.platform.core.clusters.models.PermissionData;
import com.onyxdb.platform.core.clusters.models.User;
import com.onyxdb.platform.core.clusters.models.UserToCreate;
import com.onyxdb.platform.generated.jooq.tables.records.PermissionsRecord;
import com.onyxdb.platform.generated.openapi.models.MongoUser;
import com.onyxdb.platform.generated.openapi.models.MongoUserToCreate;
import com.onyxdb.platform.utils.Consts;
import com.onyxdb.platform.utils.TimeUtils;

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
        return new com.onyxdb.platform.generated.openapi.models.MongoPermission(
                p.id(),
                p.databaseId(),
                p.createdAt(),
                p.createdBy(),
                p.isDeleted(),
                p.deletedAt(),
                p.deletedBy(),
                p.data().roles().stream().map(MongoRole::value).toList()
        );
    }

    public UserToCreate map(UUID clusterId, MongoUserToCreate u) {
        return new UserToCreate(
                clusterId,
                u.getName(),
                u.getPassword(),
                u.getPermissions().stream().map(this::map).toList()
        );
    }

    public MongoPermissionToCreate map(com.onyxdb.platform.generated.openapi.models.MongoPermissionToCreate p) {
        return new MongoPermissionToCreate(
                p.getDatabaseId(),
                p.getRoles().stream().map(MongoRole.R::fromValue).toList()
        );
    }

    public User userToCreateToUser(UserToCreate u, String passwordSecretName) {
        UUID userId = UUID.randomUUID();
        return new User(
                userId,
                u.username(),
                passwordSecretName,
                u.clusterId(),
                TimeUtils.now(),
                Consts.USER_ID,
                false,
                null,
                null,
                u.permissions().stream().map(p -> map(userId, p)).toList()
        );
    }

    public MongoPermission map(UUID userId, MongoPermissionToCreate p) {
        return new MongoPermission(
                UUID.randomUUID(),
                userId,
                p.databaseId(),
                TimeUtils.now(),
                Consts.USER_ID,
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
                    p.userId(),
                    p.databaseId(),
                    TimeUtils.now(),
                    Consts.USER_ID,
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
