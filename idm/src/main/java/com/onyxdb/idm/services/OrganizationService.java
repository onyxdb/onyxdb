package com.onyxdb.idm.services;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.Organization;
import com.onyxdb.idm.models.Resource;
import com.onyxdb.idm.repositories.OrganizationRepository;
import com.onyxdb.idm.repositories.ResourceRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final ResourceRepository resourceRepository;
    private final OrganizationRepository organizationRepository;

    public Organization findById(UUID id) {
        return organizationRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
    }

    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }

    public Organization create(Organization project) {
        Resource resource = new Resource(
                UUID.randomUUID(),
                "organization",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        resourceRepository.create(resource);

        Organization forCreate = new Organization(
                UUID.randomUUID(),
                project.name(),
                project.description(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                resource.id(),
                project.ownerId()
        );
        organizationRepository.create(forCreate);
        return forCreate;
    }

    public Organization update(Organization project) {
        Resource resource = new Resource(
                project.resourceId(),
                "organization",
                project.createdAt(),
                LocalDateTime.now()
        );
        resourceRepository.update(resource);

        Organization forUpdate = new Organization(
                project.id(),
                project.name(),
                project.description(),
                project.createdAt(),
                LocalDateTime.now(),
                project.resourceId(),
                project.ownerId()
        );
        organizationRepository.update(forUpdate);
        return forUpdate;
    }

    public void delete(UUID id) {
        organizationRepository.delete(id);
    }
}