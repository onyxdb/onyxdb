package com.onyxdb.platform.projects;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author foxleren
 */
public interface ProjectRepository {
    List<Project> list();

    Optional<Project> getO(UUID projectId);

    Project get(UUID projectId);

    void create(Project project);

    void update(UpdateProject updateProject);

    void archive(UUID id);

    void unarchive(UUID id);
}
