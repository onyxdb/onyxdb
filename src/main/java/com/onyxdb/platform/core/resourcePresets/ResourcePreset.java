package com.onyxdb.platform.core.resourcePresets;

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
