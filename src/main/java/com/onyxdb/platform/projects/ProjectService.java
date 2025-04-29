package com.onyxdb.platform.projects;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.platform.exceptions.BadRequestException;
import com.onyxdb.platform.exceptions.ProjectNotFoundException;

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

    public Optional<Project> getO(UUID id) {
        return projectRepository.getO(id);
    }

    public Project getOrThrow(UUID id) {
        return getO(id).orElseThrow(() -> new ProjectNotFoundException(id));
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
