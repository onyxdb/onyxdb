package com.onyxdb.platform.mdb.resourcePresets;

import java.util.UUID;

/**
 * @author foxleren
 */
public record ResourcePreset(
        UUID id,
        String name,
        ResourcePresetType type,
        double vcpu,
        long ram
) {
}
