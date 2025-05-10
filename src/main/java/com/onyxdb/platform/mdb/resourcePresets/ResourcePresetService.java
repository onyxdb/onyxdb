package com.onyxdb.platform.mdb.resourcePresets;

import java.util.List;
import java.util.Optional;

import com.onyxdb.platform.mdb.exceptions.ResourcePresetNotFoundException;

/**
 * @author foxleren
 */
public class ResourcePresetService {
    private final ResourcePresetRepository resourcePresetRepository;

    public ResourcePresetService(ResourcePresetRepository resourcePresetRepository) {
        this.resourcePresetRepository = resourcePresetRepository;
    }

    public List<ResourcePreset> list() {
        return resourcePresetRepository.list();
    }

    public Optional<ResourcePreset> getO(String resourcePresetId) {
        return resourcePresetRepository.getO(resourcePresetId);
    }

    public ResourcePreset getOrThrow(String resourcePresetId) {
        return getO(resourcePresetId).orElseThrow(() -> new ResourcePresetNotFoundException(resourcePresetId));
    }

    public void create(ResourcePreset resourcePreset) {
        resourcePresetRepository.create(resourcePreset);
    }

    public void update(ResourcePreset resourcePreset) {
        boolean isUpdated = resourcePresetRepository.update(resourcePreset);
        if (!isUpdated) {
            throw new ResourcePresetNotFoundException(resourcePreset.id());
        }
    }

    public void delete(String resourcePresetId) {
        boolean isDeleted = resourcePresetRepository.delete(resourcePresetId);
        if (!isDeleted) {
            throw new ResourcePresetNotFoundException(resourcePresetId);
        }
    }
}
