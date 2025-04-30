package com.onyxdb.platform.mdb.utils;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtils {
    private static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<>() {
    };

    public static Map<String, Object> convertToMap(ObjectMapper objectMapper, Object fromValue) {
        return objectMapper.convertValue(fromValue, MAP_TYPE_REFERENCE);
    }

    public static Map<String, Object> convertStringToMap(ObjectMapper objectMapper, String fromValue) {
        try {
            return objectMapper.readValue(fromValue, MAP_TYPE_REFERENCE);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String convertToString(ObjectMapper objectMapper, Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
