package com.onyxdb.mdb.hardwarePresets;

import com.onyxdb.mdb.utils.StringEnum;
import com.onyxdb.mdb.utils.StringEnumResolver;

/**
 * @author foxleren
 */
public enum HardwarePresetType implements StringEnum {
    CPU_OPTIMIZED("cpu_optimized"),
    STANDARD("standard"),
    RAM_OPTIMIZED("ram_optimized"),
    ;

    public static final StringEnumResolver<HardwarePresetType> R = new StringEnumResolver<>(HardwarePresetType.class);

    private final String value;

    HardwarePresetType(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }

    public com.onyxdb.mdb.generated.jooq.enums.HardwarePresetType toJooq() {
        return com.onyxdb.mdb.generated.jooq.enums.HardwarePresetType.lookupLiteral(value);
    }
}
