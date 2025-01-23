package com.onyxdb.mdb.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onyxdb.mdb.models.Cluster;
import com.onyxdb.mdb.processors.CompositeClusterOperationProcessor;
import com.onyxdb.mdb.repositories.ClusterRepository;

/**
 * @author foxleren
 */
@Service
@RequiredArgsConstructor
public class ClusterService {
    private final ClusterRepository clusterRepository;
//    private final CompositeClusterOperationProcessor compositeClusterOperationProcessor;
//    private final ClusterOperationService clusterOperationService;

    @Transactional
    public void create(Cluster cluster) {

        clusterRepository.createCluster(cluster);
    }
}
