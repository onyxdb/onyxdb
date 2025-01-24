package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.ProjectDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository {
    Optional<ProjectDTO> findById(UUID id);

    List<ProjectDTO> findAll();

    List<ProjectDTO> findByOrganizationId(UUID organizationId);

    void create(ProjectDTO project);

    void update(ProjectDTO project);

    void delete(UUID id);
}