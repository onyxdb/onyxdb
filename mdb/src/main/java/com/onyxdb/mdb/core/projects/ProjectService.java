package com.onyxdb.mdb.core.projects;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.mdb.exceptions.BadRequestException;

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
        return getO(id).orElseThrow(() -> new BadRequestException("Can't get project with id=" + id));
    }

    public void create(Project project) {
        projectRepository.create(project);
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
