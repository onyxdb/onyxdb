package com.onyxdb.mdb.resources;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.onyxdb.mdb.utils.StringEnum;

public enum ResourceType implements StringEnum {
    UNKNOWN("unknown", ResourceUnit.UNKNOWN),
    VCPU("vcpu", ResourceUnit.CORES),
    RAM("ram", ResourceUnit.BYTES),
    ;

    private static final Map<String, ResourceType> VALUE_TO_RESOURCE_TYPE = Arrays.stream(ResourceType.values())
            .collect(Collectors.toMap(ResourceType::value, Function.identity()));

    private final String value;
    private final ResourceUnit unit;

    ResourceType(String value, ResourceUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public ResourceUnit getUnit() {
        return unit;
    }

    public static ResourceType fromValue(String value) {
        return VALUE_TO_RESOURCE_TYPE.getOrDefault(value, ResourceType.UNKNOWN);
    }
}
