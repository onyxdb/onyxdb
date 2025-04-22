package com.onyxdb.platform.taskProcessing.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.core.clusters.ClusterService;
import com.onyxdb.platform.taskProcessing.models.Task;
import com.onyxdb.platform.taskProcessing.models.payloads.ClusterTaskPayload;

public abstract class ClusterTaskProcessor extends TaskProcessor<ClusterTaskPayload> {
    protected ClusterTaskProcessor(ObjectMapper objectMapper, ClusterService clusterService) {
        super(objectMapper, clusterService);
    }

    @Override
    protected ClusterTaskPayload parsePayload(Task task) {
        try {
            return objectMapper.readValue(task.payload(), ClusterTaskPayload.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
