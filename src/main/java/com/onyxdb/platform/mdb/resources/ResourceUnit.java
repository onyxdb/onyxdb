package com.onyxdb.platform.mdb.resources;

import com.onyxdb.platform.mdb.utils.StringEnum;
import com.onyxdb.platform.mdb.utils.StringEnumResolver;

public enum ResourceUnit implements StringEnum {
    UNKNOWN("unknown"),
    CORES("cores"),
    BYTES("bytes"),
    ;

    public static final StringEnumResolver<ResourceUnit> R = new StringEnumResolver<>(ResourceUnit.class);

    private final String value;

    ResourceUnit(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
