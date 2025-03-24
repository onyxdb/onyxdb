package com.onyxdb.mdb.core.hardwarePresets;

import java.util.List;
import java.util.Optional;

/**
 * @author foxleren
 */
public interface HardwarePresetRepository {
    List<HardwarePreset> list();

    Optional<HardwarePreset> getO(String id);

    void create(HardwarePreset hardwarePreset);

    void update(HardwarePreset hardwarePreset);

    void delete(String id);
}
