package com.onyxdb.platform.processing.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.processing.models.Task;
import com.onyxdb.platform.processing.models.payloads.ClusterPayload;

public abstract class ClusterTaskProcessor extends TaskProcessor<ClusterPayload> {
    protected ClusterTaskProcessor(ObjectMapper objectMapper, ClusterService clusterService) {
        super(objectMapper, clusterService);
    }

    @Override
    protected ClusterPayload parsePayload(Task task) {
        try {
            return objectMapper.readValue(task.payload(), ClusterPayload.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
