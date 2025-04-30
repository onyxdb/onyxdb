package com.onyxdb.platform.mdb.clients.k8s.psmdb;

import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;

public abstract class AbstractPsmdbFactory {
    protected static final TypeReference<Map<String, Object>> PROPERTY_TYPE_REF = new TypeReference<>() {
    };
    protected static final String STATUS_STATE_KEY = "state";
    protected static final String STATE_READY_VALUE = "ready";

    protected final ObjectMapper objectMapper;

    protected AbstractPsmdbFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    protected Optional<String> getStateO(GenericKubernetesResource resource) {
        Map<String, Object> statusMap = getStatusMap(resource);
        Optional<Object> stateO = Optional.ofNullable(statusMap.get(STATUS_STATE_KEY));

        return stateO.map(v -> objectMapper.convertValue(v, String.class));
    }

    protected Map<String, Object> getStatusMap(GenericKubernetesResource resource) {
        Object statusObj = resource.getAdditionalProperties().getOrDefault("status", Map.of());
        return objectMapper.convertValue(statusObj, PROPERTY_TYPE_REF);
    }
}
