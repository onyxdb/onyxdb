package com.onyxdb.mdb.hardwarePresets;

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
