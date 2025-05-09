package com.onyxdb.platform.mdb.users;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.platform.mdb.clusters.models.MongoPermission;
import com.onyxdb.platform.mdb.clusters.models.User;

public interface UserRepository {
    List<User> listUsers(
            UUID clusterId,
            String userName
    );

//    Optional<User> getUserO(UUID userId);

    Optional<User> getUserO(UUID clusterId, String userName);

//    User getUser(UUID userId);

    User getUser(UUID clusterId, String userName);

    void createUser(User user);

    void markUserAsDeleted(UUID userId, UUID deletedBy);

    void createPermissions(List<MongoPermission> permissions);

    void markPermissionsAsDeleted(List<UUID> permissionIds, UUID deletedBy);
}
