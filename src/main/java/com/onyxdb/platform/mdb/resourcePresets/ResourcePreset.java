package com.onyxdb.platform.mdb.resourcePresets;

import java.util.UUID;

/**
 * @author foxleren
 */
public record ResourcePreset(
        UUID id,
        String name,
        ResourcePresetType type,
        long vcpu,
        long ram
) {
    public static ResourcePreset create(
            String name,
            ResourcePresetType type,
            long vcpu,
            long ram
    ) {
        return new ResourcePreset(
                UUID.randomUUID(),
                name,
                type,
                vcpu,
                ram
        );
    }
}
