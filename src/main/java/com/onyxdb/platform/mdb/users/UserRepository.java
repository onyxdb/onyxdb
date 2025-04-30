package com.onyxdb.platform.mdb.users;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.onyxdb.platform.mdb.models.MongoPermission;
import com.onyxdb.platform.mdb.models.User;

public interface UserRepository {
    List<User> listUsers(
            @Nullable
            UUID clusterId,
            @Nullable
            UUID userId
    );

    Optional<User> getUserO(UUID userId);

    User getUser(UUID userId);

    void createUser(User user);

    void markUserAsDeleted(UUID userId, UUID deletedBy);

    void createPermissions(List<MongoPermission> permissions);

    void markPermissionsAsDeleted(UUID permissions);
}
