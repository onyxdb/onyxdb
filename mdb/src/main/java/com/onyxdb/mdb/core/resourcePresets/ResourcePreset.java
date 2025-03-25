package com.onyxdb.mdb.core.resourcePresets;

/**
 * @author foxleren
 */
public record ResourcePreset(
        String id,
        ResourcePresetType type,
        double vcpu,
        long ram
) {
}
