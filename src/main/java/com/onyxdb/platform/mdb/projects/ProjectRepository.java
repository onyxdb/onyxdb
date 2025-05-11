package com.onyxdb.platform.mdb.projects;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author foxleren
 */
public interface ProjectRepository {
    List<Project> listProjects();

    Optional<Project> getProjectO(UUID projectId, boolean isDeleted);

    Project getProjectOrThrow(UUID projectId, boolean isDeleted);

    Project getProjectOrThrow(UUID projectId);

    void createProject(Project project);

    boolean updateProject(UpdateProject updateProject);

    boolean markAsDeleted(UUID projectId, UUID deletedBy);
}
