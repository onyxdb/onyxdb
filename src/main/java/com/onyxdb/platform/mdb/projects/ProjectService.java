package com.onyxdb.platform.mdb.projects;

import java.util.List;
import java.util.UUID;

import com.onyxdb.platform.mdb.exceptions.ProjectNotFoundException;

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

    public Project getOrThrow(UUID projectId) {
        return projectRepository.getOrThrow(projectId);
    }

    public UUID create(CreateProject createProject) {
        Project project = projectMapper.createProjectToProject(createProject);
        projectRepository.create(project);

        return project.id();
    }

    public void update(UpdateProject updateProject) {
        boolean updated = projectRepository.update(updateProject);
        if (!updated) {
            throw new ProjectNotFoundException(updateProject.id());
        }
    }

    public void archive(UUID id) {
        projectRepository.archive(id);
    }

    public void unarchive(UUID id) {
        projectRepository.unarchive(id);
    }
}
