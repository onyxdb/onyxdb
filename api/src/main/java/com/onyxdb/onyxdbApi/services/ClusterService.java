package com.onyxdb.onyxdbApi.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.onyxdb.onyxdbApi.models.Cluster;
import com.onyxdb.onyxdbApi.repositories.ClusterRepository;

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
