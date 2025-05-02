package com.onyxdb.platform.mdb.resourcePresets;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.platform.mdb.exceptions.BadRequestException;

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

    public Optional<ResourcePreset> getO(UUID id) {
        return resourcePresetRepository.getO(id);
    }

    public ResourcePreset getOrThrow(UUID id) {
        return getO(id).orElseThrow(() -> new BadRequestException("Can't get resource preset with id=" + id));
    }

    public UUID create(ResourcePreset resourcePreset) {
        resourcePresetRepository.create(resourcePreset);

        return resourcePreset.id();
    }

    public void update(ResourcePreset resourcePreset) {
        resourcePresetRepository.update(resourcePreset);
    }

    public void delete(UUID id) {
        resourcePresetRepository.delete(id);
    }
}
