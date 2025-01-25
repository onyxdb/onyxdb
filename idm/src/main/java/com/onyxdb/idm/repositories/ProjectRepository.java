package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.Project;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository {
    Optional<Project> findById(UUID id);

    List<Project> findAll();

    List<Project> findByOrganizationId(UUID organizationId);

    void create(Project project);

    void update(Project project);

    void delete(UUID id);
}