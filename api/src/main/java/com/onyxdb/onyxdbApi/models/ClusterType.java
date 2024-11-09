package com.onyxdb.onyxdbApi.models;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author foxleren
 */
public enum ClusterType {
    MONGODB("mongodb"),
    ;

    private final String value;

    ClusterType(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static Optional<ClusterType> parseO(String value) {
        return Arrays.stream(values())
                .filter(item -> item.toString().equalsIgnoreCase(value))
                .findFirst();
    }
}
