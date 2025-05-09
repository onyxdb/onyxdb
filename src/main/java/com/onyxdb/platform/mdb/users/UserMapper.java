package com.onyxdb.platform.mdb.users;

import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.JSONB;

import com.onyxdb.platform.generated.jooq.tables.records.PermissionsRecord;
import com.onyxdb.platform.generated.openapi.models.CreateMongoPermissionDTO;
import com.onyxdb.platform.generated.openapi.models.CreateMongoUserRequestDTO;
import com.onyxdb.platform.generated.openapi.models.MongoPermissionDTO;
import com.onyxdb.platform.generated.openapi.models.MongoUserDTO;
import com.onyxdb.platform.mdb.clusters.models.CreateMongoPermission;
import com.onyxdb.platform.mdb.clusters.models.CreateUser;
import com.onyxdb.platform.mdb.clusters.models.CreateUserWithSecret;
import com.onyxdb.platform.mdb.clusters.models.MongoPermission;
import com.onyxdb.platform.mdb.clusters.models.MongoRole;
import com.onyxdb.platform.mdb.clusters.models.PermissionData;
import com.onyxdb.platform.mdb.clusters.models.User;
import com.onyxdb.platform.mdb.utils.TimeUtils;

public class UserMapper {
    private final ObjectMapper objectMapper;

    public UserMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public MongoUserDTO userToMongoUserDTO(User u) {
        return new MongoUserDTO(
                u.name(),
                u.clusterId(),
                u.createdAt(),
                u.createdBy(),
                u.isDeleted(),
                u.deletedAt(),
                u.deletedBy(),
                u.permissions().stream().map(this::mongoPermissionToMongoPermissionDTO).toList()
        );
    }

    public MongoPermissionDTO mongoPermissionToMongoPermissionDTO(MongoPermission p) {
        return new MongoPermissionDTO(
                p.databaseName(),
                p.createdAt(),
                p.createdBy(),
                p.isDeleted(),
                p.deletedAt(),
                p.deletedBy(),
                p.data().roles().stream().map(MongoRole::value).toList()
        );
    }

    public CreateUser createMongoUserRqToCreateUser(UUID clusterId, CreateMongoUserRequestDTO rq, UUID createdBy) {
        String userName = rq.getName();
        return new CreateUser(
                userName,
                rq.getPassword(),
                clusterId,
                rq.getPermissions()
                        .stream()
                        .map(p -> createMongoPermissionDTOtoCreateMongoPermission(
                                userName,
                                clusterId,
                                p
                        ))
                        .toList(),
                createdBy
        );
    }

    public CreateMongoPermission createMongoPermissionDTOtoCreateMongoPermission(
            String userName,
            UUID clusterId,
            CreateMongoPermissionDTO p
    ) {
        return new CreateMongoPermission(
                userName,
                p.getDatabaseName(),
                clusterId,
                p.getRoles().stream().map(MongoRole.R::fromValue).toList()
        );
    }

    public User createUserWithSecretToUser(CreateUserWithSecret u) {
        return new User(
                UUID.randomUUID(),
                u.userName(),
                u.passwordSecretName(),
                u.clusterId(),
                TimeUtils.now(),
                u.createdBy(),
                false,
                null,
                null,
                u.permissions()
                        .stream()
                        .map(p -> createMongoPermissionToMongoPermission(p, u.createdBy()))
                        .toList()
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
                u.createdBy(),
                false,
                null,
                null,
                u.permissions()
                        .stream()
                        .map(p -> createMongoPermissionToMongoPermission(p, u.createdBy()))
                        .toList()
        );
    }

    public MongoPermission createMongoPermissionToMongoPermission(CreateMongoPermission p, UUID createdBy) {
        return new MongoPermission(
                UUID.randomUUID(),
                p.userName(),
                p.databaseName(),
                p.clusterId(),
                TimeUtils.now(),
                createdBy,
                false,
                null,
                null,
                new PermissionData(
                        p.roles()
                )
        );
    }

    public PermissionsRecord mongoPermissionToPermissionsRecord(MongoPermission p) {
        try {
            return new PermissionsRecord(
                    p.id(),
                    p.userName(),
                    p.databaseName(),
                    p.clusterId(),
                    p.createdAt(),
                    p.createdBy(),
                    p.isDeleted(),
                    p.deletedAt(),
                    p.deletedBy(),
                    JSONB.jsonb(objectMapper.writeValueAsString(p.data()))
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
