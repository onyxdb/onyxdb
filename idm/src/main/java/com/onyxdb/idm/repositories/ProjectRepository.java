package com.onyxdb.idm.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.idm.models.ProjectDTO;

/**
 * @author ArtemFed
 */
public interface ProjectRepository {
    Optional<ProjectDTO> findById(UUID id);

    List<ProjectDTO> findByOrganizationId(UUID organizationId);

    void create(ProjectDTO project);

    void update(ProjectDTO project);

    void delete(UUID id);
}
