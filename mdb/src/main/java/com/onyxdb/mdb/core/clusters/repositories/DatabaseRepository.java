package com.onyxdb.mdb.core.clusters.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.mdb.core.clusters.models.Database;

public interface DatabaseRepository {
    List<Database> listDatabases(UUID clusterId);

    Optional<Database> getDatabaseO(UUID clusterId, UUID databaseId);

    void createDatabase(Database database);

    void markDatabaseAsDeleted(UUID databaseId, UUID deletedBy);
}
