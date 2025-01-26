package com.onyxdb.mdb.services;

import java.util.UUID;

import com.onyxdb.mdb.models.ClusterToCreate;

/**
 * @author foxleren
 */
public interface BaseClusterService {
    UUID create(ClusterToCreate clusterToCreate);
}
