package com.onyxdb.mdb.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.mdb.models.Cluster;

/**
 * @author foxleren
 */
public interface ClusterRepository {
    void create(Cluster cluster);

    Optional<Cluster> getByIdO(UUID id);

    void updateProject(UUID clusterId, UUID projectId);

    List<Cluster> getByProjectId(UUID projectId);
}
