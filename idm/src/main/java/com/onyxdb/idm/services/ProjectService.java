package com.onyxdb.idm.services;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.Resource;
import com.onyxdb.idm.models.Project;
import com.onyxdb.idm.models.Service;
import com.onyxdb.idm.repositories.OrganizationRepository;
import com.onyxdb.idm.repositories.ProjectRepository;
import com.onyxdb.idm.repositories.ResourceRepository;
import com.onyxdb.idm.repositories.ServiceRepository;
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
    private final ResourceRepository resourceRepository;
    private final OrganizationRepository organizationRepository;
    private final ServiceRepository serviceRepository;

    public Project findById(UUID id) {
        return projectRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public List<Project> findByOrganizationId(UUID organizationId) {
        return projectRepository.findByOrganizationId(organizationId);
    }

    public Project create(Project project) {
        Resource resource = new Resource(
                UUID.randomUUID(),
                "project",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        resourceRepository.create(resource);

        Project forCreate = new Project(
                UUID.randomUUID(),
                project.name(),
                project.description(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                resource.id(),
                project.organizationId(),
                project.ownerId()
        );
        projectRepository.create(forCreate);
        return forCreate;
    }

    public Project update(Project project) {
        Resource resource = new Resource(
                project.resourceId(),
                "project",
                project.createdAt(),
                LocalDateTime.now()
        );
        resourceRepository.update(resource);

        Project forUpdate = new Project(
                project.id(),
                project.name(),
                project.description(),
                project.createdAt(),
                LocalDateTime.now(),
                project.resourceId(),
                project.organizationId(),
                project.ownerId()
        );
        projectRepository.update(forUpdate);
        return forUpdate;
    }

    public void delete(UUID id) {
        projectRepository.delete(id);
    }

    public List<Service> getServicesByProjectId(UUID projectId) {
        return serviceRepository.findByProjectId(projectId);
    }
}