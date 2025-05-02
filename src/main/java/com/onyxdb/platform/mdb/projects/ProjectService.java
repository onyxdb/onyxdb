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

    public List<Project> listProjects() {
        return projectRepository.listProjects();
    }

    public Project getProjectOrThrow(UUID projectId, boolean isDeleted) {
        return projectRepository.getProjectOrThrow(projectId, isDeleted);
    }

    public Project getUndeletedProjectOrThrow(UUID projectId) {
        return projectRepository.getProjectOrThrow(projectId, false);
    }

    public UUID createProject(CreateProject createProject) {
        Project project = projectMapper.createProjectToProject(createProject);
        projectRepository.createProject(project);

        return project.id();
    }

    public void updateProject(UpdateProject updateProject) {
        boolean isUpdated = projectRepository.updateProject(updateProject);
        if (!isUpdated) {
            throw new ProjectNotFoundException(updateProject.id());
        }
    }

    public void deleteProject(UUID projectId, UUID deletedBy) {
        boolean isDeleted = projectRepository.markAsDeleted(projectId, deletedBy);
        if (!isDeleted) {
            throw new ProjectNotFoundException(projectId);
        }
    }
}
