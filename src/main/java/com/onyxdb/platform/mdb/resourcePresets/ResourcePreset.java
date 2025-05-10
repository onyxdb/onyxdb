package com.onyxdb.platform.mdb.resourcePresets;

/**
 * @author foxleren
 */
public record ResourcePreset(
        String id,
        ResourcePresetType type,
        long vcpu,
        long ram
) {
}
