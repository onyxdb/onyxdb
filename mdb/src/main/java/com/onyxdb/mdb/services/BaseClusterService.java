package com.onyxdb.mdb.services;

import java.util.Optional;
import java.util.UUID;

import com.onyxdb.mdb.models.Cluster;
import com.onyxdb.mdb.models.ClusterToCreate;

/**
 * @author foxleren
 */
public interface BaseClusterService {
    UUID create(ClusterToCreate clusterToCreate);

    Optional<Cluster> getByIdO(UUID id);
}
