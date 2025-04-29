package com.onyxdb.platform.projects;

import java.util.List;
import java.util.UUID;

/**
 * @author foxleren
 */
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> list() {
        return projectRepository.list();
    }

    public Project get(UUID projectId) {
        return projectRepository.get(projectId);
    }

    public void create(ProjectToCreate projectToCreate) {
        projectRepository.create(projectToCreate);
    }

    public void update(UpdateProject updateProject) {
        projectRepository.update(updateProject);
    }

    public void archive(UUID id) {
        projectRepository.archive(id);
    }

    public void unarchive(UUID id) {
        projectRepository.unarchive(id);
    }
}
