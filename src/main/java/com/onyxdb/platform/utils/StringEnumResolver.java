package com.onyxdb.platform.utils;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.onyxdb.platform.exceptions.BadRequestException;

/**
 * @author foxleren
 */
public class StringEnumResolver<T extends Enum<T> & StringEnum> {
    private final Class<T> enumClass;
    private final Map<String, T> stringValueToEnum;

    public StringEnumResolver(Class<T> enumClass) {
        this.enumClass = enumClass;
        this.stringValueToEnum = Arrays.stream(enumClass.getEnumConstants())
                .collect(Collectors.toMap(StringEnum::value, Function.identity()));
    }

    public Optional<T> fromValueO(String value) {
        return Optional.ofNullable(stringValueToEnum.get(value));
    }

    public T fromValue(String value) {
        return fromValueO(value).orElseThrow(() -> new BadRequestException(
                String.format("Can't convert value=%s to enum=%s", value, enumClass.getName())
        ));
    }

    public T fromValueOrDefault(String value, T defaultEnum) {
        return fromValueO(value).orElse(defaultEnum);
    }
}
