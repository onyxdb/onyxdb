package com.onyxdb.platform.operationsOLD;

import java.util.UUID;

import com.onyxdb.platform.processing.models.OperationType;

public record OperationV2(
        UUID id,
        OperationType type,
        String payload
) {
//    public <T extends OperationPayload> T getParsedPayload(ObjectMapper mapper) {
//        try {
//            return mapper.readValue(payload, type.getPayloadClazz());
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
