package com.onyxdb.platform.mdb.operations.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.operations.models.Task;
import com.onyxdb.platform.mdb.operations.models.payload.ClusterPayload;

public abstract class ClusterTaskConsumer extends TaskConsumer<ClusterPayload> {
    protected ClusterTaskConsumer(ObjectMapper objectMapper, ClusterService clusterService) {
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
