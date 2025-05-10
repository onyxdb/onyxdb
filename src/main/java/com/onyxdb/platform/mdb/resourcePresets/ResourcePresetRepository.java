package com.onyxdb.platform.mdb.resourcePresets;

import java.util.List;
import java.util.Optional;

/**
 * @author foxleren
 */
public interface ResourcePresetRepository {
    List<ResourcePreset> list();

    Optional<ResourcePreset> getO(String resourcePresetId);

    void create(ResourcePreset resourcePreset);

    boolean update(ResourcePreset resourcePreset);

    boolean delete(String resourcePresetId);
}
