package com.onyxdb.platform.utils;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtils {
    private static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<>() {
    };

    public static Map<String, Object> convertToMap(ObjectMapper objectMapper, Object fromValue) {
        return objectMapper.convertValue(fromValue, MAP_TYPE_REFERENCE);
    }
}
