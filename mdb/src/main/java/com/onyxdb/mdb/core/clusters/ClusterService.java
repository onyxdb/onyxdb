package com.onyxdb.mdb.core.clusters;

import java.util.UUID;

import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.mdb.core.clusters.models.Cluster;
import com.onyxdb.mdb.core.clusters.models.CreateCluster;
import com.onyxdb.mdb.core.clusters.repositories.MongoClusterRepository;

/**
 * @author foxleren
 */
public class ClusterService {
    private final ClusterMapper clusterMapper;
    private final MongoClusterRepository mongoClusterRepository;
    private final TransactionTemplate transactionTemplate;

    public ClusterService(
            ClusterMapper clusterMapper,
            MongoClusterRepository mongoClusterRepository,
            TransactionTemplate transactionTemplate
    ) {
        this.clusterMapper = clusterMapper;
        this.mongoClusterRepository = mongoClusterRepository;
        this.transactionTemplate = transactionTemplate;
    }

    public UUID createCluster(CreateCluster createCluster) {
        Cluster cluster = clusterMapper.createClusterToCluster(createCluster);

        transactionTemplate.executeWithoutResult(status -> {
            mongoClusterRepository.createCluster(cluster);
            mongoClusterRepository.createMongoV8d0Config(cluster.id(), cluster.config().mongoV8d0());
        });

        return cluster.id();
    }
}
