package com.onyxdb.platform.mdb.databases;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.platform.core.clusters.models.Database;

public interface DatabaseRepository {
    List<Database> listDatabases(UUID clusterId);

    Optional<Database> getDatabaseO(UUID clusterId, UUID databaseId);

    Database getDatabase(UUID clusterId, UUID databaseId);

    void createDatabase(Database database);

    void markDatabaseAsDeleted(UUID databaseId, UUID deletedBy);
}
