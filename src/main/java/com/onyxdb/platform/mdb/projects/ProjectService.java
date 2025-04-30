package com.onyxdb.platform.mdb.projects;

import java.util.List;
import java.util.UUID;

/**
 * @author foxleren
 */
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    public List<Project> list() {
        return projectRepository.list();
    }

    public Project get(UUID projectId) {
        return projectRepository.get(projectId);
    }

    public UUID create(ProjectToCreate projectToCreate) {
        Project project = projectMapper.projectToCreateToProject(projectToCreate);
        projectRepository.create(project);

        return project.id();
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
