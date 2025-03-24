package com.onyxdb.mdb.utils;

import java.util.Optional;

/**
 * @author foxleren
 */
public interface StringEnum {
    String value();

    static <E extends Enum<E>> Optional<E> fromValueO(Class<E> enumClass, Object value) {
        for (E enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.toString().equalsIgnoreCase(value.toString())) {
                return Optional.of(enumConstant);
            }
        }
        return Optional.empty();
    }

    static <E extends Enum<E>> E fromValue(Class<E> enumClass, Object value) {
        return fromValueO(enumClass, value).orElseThrow(() -> new IllegalArgumentException(
                String.format("Can't convert value=%s to enum=%s", value, enumClass.getName())
        ));
    }
}
