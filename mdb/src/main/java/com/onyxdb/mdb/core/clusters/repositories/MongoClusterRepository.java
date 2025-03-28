package com.onyxdb.mdb.core.clusters.repositories;

import java.util.UUID;

import com.onyxdb.mdb.core.clusters.models.MongoV8d0Config;

/**
 * @author foxleren
 */
public interface MongoClusterRepository extends ClusterRepository {
    void createMongoV8d0Config(
            UUID clusterId,
            MongoV8d0Config mongoV8d0Config
    );
}
