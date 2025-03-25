package com.onyxdb.mdb.core.resourcePresets;

import java.util.List;
import java.util.Optional;

/**
 * @author foxleren
 */
public interface ResourcePresetRepository {
    List<ResourcePreset> list();

    Optional<ResourcePreset> getO(String id);

    void create(ResourcePreset resourcePreset);

    void update(ResourcePreset resourcePreset);

    void delete(String id);
}
