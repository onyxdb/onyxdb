package com.onyxdb.mdb.core.resourcePresets;

import java.util.List;
import java.util.Optional;

import com.onyxdb.mdb.exceptions.BadRequestException;

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

    public Optional<ResourcePreset> getO(String id) {
        return resourcePresetRepository.getO(id);
    }

    public ResourcePreset getOrThrow(String id) {
        return getO(id).orElseThrow(() -> new BadRequestException("Can't get resource preset with id=" + id));
    }

    public void create(ResourcePreset resourcePreset) {
        resourcePresetRepository.create(resourcePreset);
    }

    public void update(ResourcePreset resourcePreset) {
        resourcePresetRepository.update(resourcePreset);
    }

    public void delete(String id) {
        resourcePresetRepository.delete(id);
    }
}
