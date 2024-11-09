package com.onyxdb.mdb.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.onyxdb.mdb.models.Cluster;
import com.onyxdb.mdb.repositories.ClusterRepository;

/**
 * @author foxleren
 */
@Service
@RequiredArgsConstructor
public class ClusterService {
    private final ClusterRepository clusterRepository;

    public void createCluster(Cluster cluster) {
        clusterRepository.createCluster(cluster);
    }
}
