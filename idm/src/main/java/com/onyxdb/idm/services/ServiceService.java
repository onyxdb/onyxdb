package com.onyxdb.idm.services;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.Resource;
import com.onyxdb.idm.models.Service;
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
public class ServiceService {
    private final ServiceRepository serviceRepository;
    private final ResourceRepository resourceRepository;

    public Service findById(UUID id) {
        return serviceRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
    }

    public List<Service> findAll() {
        return serviceRepository.findAll();
    }

    public List<Service> findByProjectId(UUID projectId) {
        return serviceRepository.findByProjectId(projectId);
    }

    public Service create(Service service) {
        Resource resource = new Resource(
                UUID.randomUUID(),
                "service",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        resourceRepository.create(resource);

        Service forCreate = new Service(
                UUID.randomUUID(),
                service.name(),
                service.type(),
                service.description(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                resource.id(),
                service.projectId(),
                service.ownerId()
        );
        serviceRepository.create(forCreate);
        return forCreate;
    }

    public Service update(Service service) {
        Resource resource = new Resource(
                service.resourceId(),
                "service",
                service.createdAt(),
                LocalDateTime.now()
        );
        resourceRepository.update(resource);

        Service forUpdate = new Service(
                service.id(),
                service.name(),
                service.type(),
                service.description(),
                service.createdAt(),
                LocalDateTime.now(),
                service.resourceId(),
                service.projectId(),
                service.ownerId()
        );
        serviceRepository.update(forUpdate);
        return forUpdate;
    }

    public void delete(UUID id) {
        serviceRepository.delete(id);
    }
}