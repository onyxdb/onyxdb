package com.onyxdb.platform.core.resourcePresets;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author foxleren
 */
public interface ResourcePresetRepository {
    List<ResourcePreset> list();

    Optional<ResourcePreset> getO(UUID id);

    void create(ResourcePreset resourcePreset);

    void update(ResourcePreset resourcePreset);

    void delete(UUID id);
}
