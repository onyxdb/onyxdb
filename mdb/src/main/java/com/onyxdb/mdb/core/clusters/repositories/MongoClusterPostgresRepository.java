package com.onyxdb.mdb.core.clusters.repositories;

import java.util.UUID;

import org.jooq.DSLContext;

import com.onyxdb.mdb.core.clusters.ClusterMapper;
import com.onyxdb.mdb.core.clusters.models.ClusterResources;
import com.onyxdb.mdb.core.clusters.models.MongoV8d0Config;
import com.onyxdb.mdb.core.clusters.models.MongodV8d0;
import com.onyxdb.mdb.core.clusters.models.MongodV8d0Config;

import static com.onyxdb.mdb.generated.jooq.Tables.MONGO_8_0_CONFIGS;

/**
 * @author foxleren
 */
public class MongoClusterPostgresRepository extends ClusterPostgresRepository implements MongoClusterRepository {
    public MongoClusterPostgresRepository(
            DSLContext dslContext,
            ClusterMapper clusterMapper
    ) {
        super(dslContext, clusterMapper);
    }

    @Override
    public void createMongoV8d0Config(
            UUID clusterId,
            MongoV8d0Config mongoV8d0Config
    ) {
        MongodV8d0 mongod = mongoV8d0Config.mongod();
        ClusterResources resources = mongod.resources();
        MongodV8d0Config mongodConfig = mongod.config();
        dslContext.insertInto(MONGO_8_0_CONFIGS)
                .columns(
                        MONGO_8_0_CONFIGS.CLUSTER_ID,
                        MONGO_8_0_CONFIGS.MONGOD_RESOURCES_PRESET_ID,
                        MONGO_8_0_CONFIGS.MONGOD_STORAGE_CLASS,
                        MONGO_8_0_CONFIGS.MONGOD_STORAGE,
                        MONGO_8_0_CONFIGS.MONGOD_CFG_NET_MAX_INCOMING_CONNECTIONS
                )
                .values(
                        clusterId,
                        resources.presetId(),
                        resources.storageClass(),
                        resources.storage(),
                        mongodConfig.net().maxIncomingConnections()
                )
                .execute();
    }
}
