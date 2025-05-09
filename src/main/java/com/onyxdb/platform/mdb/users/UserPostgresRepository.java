package com.onyxdb.platform.mdb.users;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.springframework.dao.DuplicateKeyException;

import com.onyxdb.platform.generated.jooq.Indexes;
import com.onyxdb.platform.generated.jooq.tables.records.PermissionsRecord;
import com.onyxdb.platform.mdb.clusters.models.MongoPermission;
import com.onyxdb.platform.mdb.clusters.models.PermissionData;
import com.onyxdb.platform.mdb.clusters.models.User;
import com.onyxdb.platform.mdb.exceptions.UserAlreadyExistsException;
import com.onyxdb.platform.mdb.exceptions.UserNotFoundException;
import com.onyxdb.platform.mdb.utils.PsqlUtils;
import com.onyxdb.platform.mdb.utils.TimeUtils;

import static com.onyxdb.platform.generated.jooq.Tables.PERMISSIONS;
import static com.onyxdb.platform.generated.jooq.Tables.USERS;

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
            UUID clusterId,
            String userName
    ) {
        var condition = DSL.trueCondition()
                .and(USERS.IS_DELETED.eq(false)
                        .and(PERMISSIONS.IS_DELETED.eq(false)));

        if (clusterId != null) {
            condition = condition.and(USERS.CLUSTER_ID.eq(clusterId));
        }
        if (userName != null) {
            condition = condition.and(USERS.NAME.eq(userName));
        }

        Map<UUID, List<User>> userIdToUsers = dslContext.select()
                .from(USERS)
                .join(PERMISSIONS)
                .on(USERS.CLUSTER_ID.eq(PERMISSIONS.CLUSTER_ID).and(USERS.NAME.eq(PERMISSIONS.USER_NAME)))
                .where(condition)
                .fetch()
                .map(r -> {
                    try {
                        MongoPermission permission = new MongoPermission(
                                r.get(PERMISSIONS.ID),
                                r.get(PERMISSIONS.USER_NAME),
                                r.get(PERMISSIONS.DATABASE_NAME),
                                r.get(PERMISSIONS.CLUSTER_ID),
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
                                r.get(USERS.PASSWORD_SECRET),
                                r.get(USERS.CLUSTER_ID),
                                r.get(USERS.CREATED_AT),
                                r.get(USERS.CREATED_BY),
                                r.get(USERS.IS_DELETED),
                                r.get(USERS.DELETED_AT),
                                r.get(USERS.DELETED_BY),
                                List.of(permission)
                        );


//                        @Nullable
//                        User userFromMap = userIdToUser.get(user.id());
//                        if (userFromMap != null) {
//                            userFromMap.permissions().add(permission);
//                            userIdToUser.put(user.id(), userFromMap);
//                        } else {
//                            userIdToUser.put(user.id(), user);
//                        }

                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .stream()
                .collect(Collectors.groupingBy(User::id));

        return userIdToUsers.values().stream().map(groupedUsers -> {
                    User firstUser = groupedUsers.getFirst();
                    List<MongoPermission> permissions = groupedUsers.stream()
                            .map(User::permissions)
                            .flatMap(Collection::stream)
                            .toList();

                    return User.builder()
                            .copy(firstUser)
                            .permissions(permissions)
                            .build();
                })
                .sorted(Comparator.comparing(User::createdAt))
                .toList();
    }

//    @Override
//    public Optional<User> getUserO(UUID userId) {

    /// /        return listUsers(null, userId).stream().findFirst();
//        throw new NotImplementedException();
//    }
    @Override
    public Optional<User> getUserO(UUID clusterId, String userName) {
        return listUsers(clusterId, userName).stream().findFirst();
    }

//    @Override
//    public User getUser(UUID userId) {
//        return getUserO(userId).orElseThrow(() -> new UserNotFoundException(userId));
//    }

    @Override
    public User getUser(UUID clusterId, String userName) {
        return getUserO(clusterId, userName).orElseThrow(() -> new UserNotFoundException(userName));
    }

    @Override
    public void createUser(User user) {
        try {
            dslContext.insertInto(USERS)
                    .columns(
                            USERS.ID,
                            USERS.NAME,
                            USERS.PASSWORD_SECRET,
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
                            user.passwordSecretName(),
                            user.clusterId(),
                            user.createdAt(),
                            user.createdBy(),
                            user.isDeleted(),
                            user.deletedAt(),
                            user.deletedBy()
                    )
                    .execute();
        } catch (DataAccessException | DuplicateKeyException e) {
            PsqlUtils.handleDataAccessEx(
                    e,
                    USERS,
                    Indexes.USERS_USER_NAME_CLUSTER_ID_IS_DELETED_UNIQ_IDX,
                    () -> new UserAlreadyExistsException(user.name())
            );

            throw e;
        }
    }

    @Override
    public void markUserAsDeleted(UUID userId, UUID deletedBy) {
        dslContext.update(USERS)
                .set(USERS.IS_DELETED, true)
                .set(USERS.DELETED_AT, TimeUtils.now())
                .set(USERS.DELETED_BY, deletedBy)
                .where(USERS.ID.eq(userId))
                .execute();
    }

    @Override
    public void createPermissions(List<MongoPermission> permissions) {
        List<PermissionsRecord> records = permissions.stream().map(userMapper::mongoPermissionToPermissionsRecord).toList();
        dslContext.batchInsert(records).execute();
    }

    @Override
    public void markPermissionsAsDeleted(List<UUID> permissionIds, UUID deletedBy) {
        dslContext.update(PERMISSIONS)
                .set(PERMISSIONS.IS_DELETED, true)
                .set(PERMISSIONS.DELETED_AT, TimeUtils.now())
                .set(PERMISSIONS.DELETED_BY, deletedBy)
                .where(PERMISSIONS.ID.in(permissionIds))
                .execute();
//        throw new RuntimeException("FIXME");
    }
}
