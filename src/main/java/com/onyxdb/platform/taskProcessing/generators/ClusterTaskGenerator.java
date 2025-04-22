package com.onyxdb.platform.taskProcessing.generators;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.taskProcessing.models.payloads.ClusterTaskPayload;

public abstract class ClusterTaskGenerator extends TaskGenerator<ClusterTaskPayload> {
    protected ClusterTaskGenerator(ObjectMapper objectMapper) {
        super(objectMapper);
    }
}
