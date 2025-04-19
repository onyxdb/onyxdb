package com.onyxdb.mdb.core.clusters.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.onyxdb.mdb.core.clusters.models.MongoPermission;
import com.onyxdb.mdb.core.clusters.models.User;

public interface UserRepository {
    List<User> listUsers(
            @Nullable
            UUID clusterId,
            @Nullable
            UUID userId
    );

    Optional<User> getUserO(UUID userId);

    void createUser(User user);

    void markUserAsDeleted(UUID userId);

    void createPermissions(List<MongoPermission> permissions);

    void markPermissionsAsDeleted(UUID permissions);
}
