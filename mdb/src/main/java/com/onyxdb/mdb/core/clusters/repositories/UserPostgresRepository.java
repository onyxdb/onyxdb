package com.onyxdb.mdb.core.clusters.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.Nullable;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

import com.onyxdb.mdb.core.clusters.mappers.UserMapper;
import com.onyxdb.mdb.core.clusters.models.MongoPermission;
import com.onyxdb.mdb.core.clusters.models.PermissionData;
import com.onyxdb.mdb.core.clusters.models.User;
import com.onyxdb.mdb.exceptions.BadRequestException;
import com.onyxdb.mdb.generated.jooq.Indexes;
import com.onyxdb.mdb.generated.jooq.tables.records.PermissionsRecord;
import com.onyxdb.mdb.utils.PsqlUtils;

import static com.onyxdb.mdb.generated.jooq.Tables.PERMISSIONS;
import static com.onyxdb.mdb.generated.jooq.Tables.USERS;

public class UserPostgresRepository implements UserRepository {
    private final DSLContext dslContext;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;

    public UserPostgresRepository(
            DSLContext dslContext,
            UserMapper userMapper,
            ObjectMapper objectMapper
    ) {
        this.dslContext = dslContext;
        this.userMapper = userMapper;
        this.objectMapper = objectMapper;
    }

    // TODO use filter builder for search
    @Override
    public List<User> listUsers(
            @Nullable
            UUID clusterId,
            @Nullable
            UUID userId
    ) {
        var condition = DSL.trueCondition()
                .and(USERS.IS_DELETED.eq(false)
                .and(PERMISSIONS.IS_DELETED.eq(false)));

        if (clusterId != null) {
            condition = condition.and(USERS.CLUSTER_ID.eq(clusterId));
        }
        if (userId != null) {
            condition = condition.and(USERS.ID.eq(userId));
        }

        USERS.CLUSTER_ID.eq(clusterId)
                .and(USERS.IS_DELETED.eq(false));
        return dslContext.select()
                .from(USERS)
                .join(PERMISSIONS)
                .on(USERS.ID.eq(PERMISSIONS.USER_ID))
                .where(condition)
                .fetch()
                .map(r -> {
                    try {
                        MongoPermission permission = new MongoPermission(
                                r.get(PERMISSIONS.ID),
                                r.get(PERMISSIONS.USER_ID),
                                r.get(PERMISSIONS.DATABASE_ID),
                                r.get(PERMISSIONS.CREATED_AT),
                                r.get(PERMISSIONS.CREATED_BY),
                                r.get(PERMISSIONS.IS_DELETED),
                                r.get(PERMISSIONS.DELETED_AT),
                                r.get(PERMISSIONS.DELETED_BY),
                                objectMapper.readValue((r.get(PERMISSIONS.DATA)).data(), PermissionData.class)
                        );

                        return new User(
                                r.get(USERS.ID),
                                r.get(USERS.NAME),
                                r.get(USERS.CLUSTER_ID),
                                r.get(USERS.CREATED_AT),
                                r.get(USERS.CREATED_BY),
                                r.get(USERS.IS_DELETED),
                                r.get(USERS.DELETED_AT),
                                r.get(USERS.DELETED_BY),
                                List.of(permission)
                        );
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public Optional<User> getUserO(UUID userId) {
        return listUsers(null, userId).stream().findFirst();
    }

    @Override
    public void createUser(User user) {
        try {
        dslContext.insertInto(USERS)
                .columns(
                        USERS.ID,
                        USERS.NAME,
                        USERS.CLUSTER_ID,
                        USERS.CREATED_AT,
                        USERS.CREATED_BY,
                        USERS.IS_DELETED,
                        USERS.DELETED_AT,
                        USERS.DELETED_BY
                )
                .values(
                        user.id(),
                        user.name(),
                        user.clusterId(),
                        user.createdAt(),
                        user.createdBy(),
                        user.isDeleted(),
                        user.deletedAt(),
                        user.deletedBy()
                )
                .execute();
        } catch (DataAccessException e) {
            PsqlUtils.handleDataAccessEx(
                    e,
                    USERS,
                    Indexes.USERS_NAME_CLUSTER_ID_IDX,
                    () -> new BadRequestException(String.format(
                            "User with name '%s' already exists",
                            user.name()
                    ))
            );

            throw e;
        }
    }

    @Override
    public void markUserAsDeleted(UUID userId) {
        dslContext.update(USERS)
                .set(USERS.IS_DELETED, true)
                .where(USERS.ID.eq(userId))
                .execute();
    }

    @Override
    public void createPermissions(List<MongoPermission> permissions) {
        List<PermissionsRecord> records = permissions.stream().map(userMapper::mapToPermissionsRecord).toList();
        dslContext.batchInsert(records).execute();
    }

    @Override
    public void markPermissionsAsDeleted(UUID userId) {
        dslContext.update(PERMISSIONS)
                .set(PERMISSIONS.IS_DELETED, true)
                .where(PERMISSIONS.USER_ID.eq(userId))
                .execute();
    }
}
