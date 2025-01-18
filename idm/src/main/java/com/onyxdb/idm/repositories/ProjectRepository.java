package com.onyxdb.idm.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.idm.models.Project;

/**
 * @author ArtemFed
 */
public interface ProjectRepository {
    Optional<Project> findById(UUID id);

    List<Project> findByOrganizationId(UUID organizationId);

    void create(Project project);

    void update(Project project);

    void delete(UUID id);
}
