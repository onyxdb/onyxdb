package com.onyxdb.mdb.core.hardwarePresets;

/**
 * @author foxleren
 */
public record HardwarePreset(
        String id,
        HardwarePresetType type,
        double vcpu,
        long ram
) {
}
