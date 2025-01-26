package com.onyxdb.idm.services;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.Resource;
import com.onyxdb.idm.repositories.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResourceService {
    private final ResourceRepository resourceRepository;

    public Resource findById(UUID id) {
        Optional<Resource> resource = resourceRepository.findById(id);
        return resource.orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
    }

    public List<Resource> findAll() {
        return resourceRepository.findAll();
    }

    public Resource create(Resource resource) {
        Resource forCreate = new Resource(
                UUID.randomUUID(),
                resource.resourceType(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        resourceRepository.create(forCreate);
        return forCreate;
    }

    public Resource update(Resource resource) {
        Resource forUpdate = new Resource(
                resource.id(),
                resource.resourceType(),
                resource.createdAt(),
                LocalDateTime.now()
        );
        resourceRepository.update(forUpdate);
        return forUpdate;
    }

    public void delete(UUID id) {
        resourceRepository.delete(id);
    }
}