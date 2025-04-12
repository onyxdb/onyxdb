package com.onyxdb.mdb.taskProcessing.generators;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.mdb.taskProcessing.models.payloads.ClusterTaskPayload;

public abstract class ClusterTaskGenerator extends TaskGenerator<ClusterTaskPayload> {
    protected ClusterTaskGenerator(ObjectMapper objectMapper) {
        super(objectMapper);
    }
}
