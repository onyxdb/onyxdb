package com.onyxdb.platform.mdb.resourcePresets;

import com.onyxdb.platform.mdb.utils.StringEnum;
import com.onyxdb.platform.mdb.utils.StringEnumResolver;

/**
 * @author foxleren
 */
public enum ResourcePresetType implements StringEnum {
    CPU_OPTIMIZED("cpu_optimized"),
    STANDARD("standard"),
    RAM_OPTIMIZED("ram_optimized"),
    ;

    public static final StringEnumResolver<ResourcePresetType> R = new StringEnumResolver<>(ResourcePresetType.class);

    private final String value;

    ResourcePresetType(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }

    public com.onyxdb.platform.generated.jooq.enums.ResourcePresetType toJooq() {
        return com.onyxdb.platform.generated.jooq.enums.ResourcePresetType.lookupLiteral(value);
    }
}
