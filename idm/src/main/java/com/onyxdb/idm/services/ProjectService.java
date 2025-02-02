package com.onyxdb.idm.services;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.Project;
import com.onyxdb.idm.repositories.ProjectRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author ArtemFed
 */
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public Project findById(UUID id) {
        return projectRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project create(Project project) {
        Project forCreate = new Project(
                UUID.randomUUID(),
                project.name(),
                project.description(),
                project.parent_id(),
                project.ownerId(),
                project.data(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        projectRepository.create(forCreate);
        return forCreate;
    }

    public Project update(Project project) {
        Project forUpdate = new Project(
                project.id(),
                project.name(),
                project.description(),
                project.parent_id(),
                project.ownerId(),
                project.data(),
                project.createdAt(),
                LocalDateTime.now()
        );
        projectRepository.update(forUpdate);
        return forUpdate;
    }

    public void delete(UUID id) {
        projectRepository.delete(id);
    }
}