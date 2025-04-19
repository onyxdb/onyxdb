package com.onyxdb.mdb.core.clusters.mappers;

import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.JSONB;

import com.onyxdb.mdb.core.clusters.models.MongoPermission;
import com.onyxdb.mdb.core.clusters.models.MongoPermissionToCreate;
import com.onyxdb.mdb.core.clusters.models.MongoRole;
import com.onyxdb.mdb.core.clusters.models.PermissionData;
import com.onyxdb.mdb.core.clusters.models.User;
import com.onyxdb.mdb.core.clusters.models.UserToCreate;
import com.onyxdb.mdb.generated.jooq.tables.records.PermissionsRecord;
import com.onyxdb.mdb.generated.openapi.models.MongoUser;
import com.onyxdb.mdb.generated.openapi.models.MongoUserToCreate;
import com.onyxdb.mdb.utils.Consts;
import com.onyxdb.mdb.utils.TimeUtils;

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

    public com.onyxdb.mdb.generated.openapi.models.MongoPermission map(MongoPermission p) {
        return new com.onyxdb.mdb.generated.openapi.models.MongoPermission(
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
                u.getName(),
                u.getPassword(),
                clusterId,
                u.getPermissions().stream().map(this::map).toList()
        );
    }

    public MongoPermissionToCreate map(com.onyxdb.mdb.generated.openapi.models.MongoPermissionToCreate p) {
        return new MongoPermissionToCreate(
                p.getDatabaseId(),
                p.getRoles().stream().map(MongoRole.R::fromValue).toList()
        );
    }

    public User map(UserToCreate u) {
        UUID userId = UUID.randomUUID();
        return new User(
                userId,
                u.name(),
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
