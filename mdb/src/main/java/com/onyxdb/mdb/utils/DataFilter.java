package com.onyxdb.mdb.utils;

import java.util.List;
import java.util.Optional;

/**
 * @author foxleren
 */
public class DataFilter {
    public static <T> T single(List<T> items) {
        if (items.size() != 1) {
            throw new IllegalArgumentException("List must contain single result");
        }

        return items.getFirst();
    }

    public static <T> Optional<T> singleO(List<T> items) {
        if (items.size() != 1) {
            return Optional.empty();
        }

        return Optional.of(items.getFirst());
    }
}
