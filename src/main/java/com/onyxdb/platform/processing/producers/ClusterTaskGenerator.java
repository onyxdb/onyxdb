package com.onyxdb.platform.processing.producers;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.processing.models.payloads.ClusterPayload;

public abstract class ClusterTaskGenerator extends TaskProducer<ClusterPayload> {
    protected ClusterTaskGenerator(ObjectMapper objectMapper) {
        super(objectMapper);
    }
}
