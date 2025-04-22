package com.onyxdb.mdb.resources;

import java.util.List;

import com.onyxdb.mdb.utils.DataFilter;

public class ResourceService {
    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public List<Resource> listResources(ResourceFilter filter) {
        return resourceRepository.listResources(filter);
    }

    public Resource getOrThrow(ResourceFilter filter) {
        return DataFilter.single(listResources(filter));
    }
}
