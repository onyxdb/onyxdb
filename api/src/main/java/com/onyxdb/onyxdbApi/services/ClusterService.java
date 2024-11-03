package com.onyxdb.onyxdbApi.services;

import com.onyxdb.onyxdbApi.models.Cluster;
import com.onyxdb.onyxdbApi.repository.ClusterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
