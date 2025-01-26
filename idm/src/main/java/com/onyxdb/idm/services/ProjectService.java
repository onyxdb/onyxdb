package com.onyxdb.idm.services;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.Project;
import com.onyxdb.idm.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public Project findById(UUID id) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isPresent()) {
            return project.get();
        } else {
            throw new ResourceNotFoundException("Project not found");
        }
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public void create(Project project) {
        Project forCreate = new Project(
                UUID.randomUUID(),
                project.name(),
                project.description(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                project.resourceId(),
                project.organizationId(),
                project.ownerId()
        );
        projectRepository.create(forCreate);
    }

    public void update(Project project) {
        Project forUpdate = new Project(
                UUID.randomUUID(),
                project.name(),
                project.description(),
                project.createdAt(),
                LocalDateTime.now(),
                project.resourceId(),
                project.organizationId(),
                project.ownerId()
        );
        projectRepository.update(forUpdate);
    }

    public void delete(UUID id) {
        projectRepository.delete(id);
    }
}